package com.leepandar.starter.storage.core;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import com.leepandar.starter.core.constant.StringConstants;
import com.leepandar.starter.storage.autoconfigure.properties.StorageProperties;
import com.leepandar.starter.storage.common.constant.StorageConstant;
import com.leepandar.starter.storage.common.exception.StorageException;
import com.leepandar.starter.storage.domain.file.EnhancedMultipartFile;
import com.leepandar.starter.storage.domain.file.FileWrapper;
import com.leepandar.starter.storage.domain.file.ProgressAwareMultipartFile;
import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.domain.model.req.ThumbnailInfo;
import com.leepandar.starter.storage.domain.model.resp.*;
import com.leepandar.starter.storage.processor.preprocess.*;
import com.leepandar.starter.storage.processor.progress.UploadProgressListener;
import com.leepandar.starter.storage.processor.registry.ProcessorRegistry;
import com.leepandar.starter.storage.engine.StorageStrategyRouter;
import com.leepandar.starter.storage.service.FileProcessor;
import com.leepandar.starter.storage.service.FileRecorder;
import com.leepandar.starter.storage.strategy.StorageStrategy;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 增强版文件存储服务
 * 支持链式调用和更多功能
 *
 * @author echo
 * @since 2.14.0
 */
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private final StorageStrategyRouter router;
    private final StorageProperties storageProperties;
    private final ProcessorRegistry processorRegistry;
    private final FileRecorder fileRecorder;
    private final ThreadLocal<List<FileProcessor>> tempProcessors = ThreadLocal.withInitial(ArrayList::new);
    private final ThreadLocal<UploadProgressListener> progressListener = new ThreadLocal<>();

    public FileStorageService(StorageStrategyRouter router,
                              StorageProperties storageProperties,
                              ProcessorRegistry processorRegistry,
                              FileRecorder fileRecorder) {
        this.router = router;
        this.storageProperties = storageProperties;
        this.processorRegistry = processorRegistry;
        this.fileRecorder = fileRecorder;
    }

    /**
     * 获取默认存储平台
     */
    public String getDefaultPlatform() {
        return router.getDefaultStorage();
    }

    /**
     * 获取处理器注册表
     */
    public ProcessorRegistry getProcessorRegistry() {
        return processorRegistry;
    }

    /**
     * MultipartFile 直接上传
     */
    public UploadPretreatment of(MultipartFile file) {
        return createPretreatment(file, null);
    }

    /**
     * MultipartFile 指定平台
     */
    public UploadPretreatment of(MultipartFile file, String platform) {
        return createPretreatment(file, platform);
    }

    /**
     * byte[] 上传
     */
    public UploadPretreatment of(byte[] bytes, String filename, String contentType) {
        return createPretreatment(bytes, filename, contentType);
    }

    /**
     * InputStream 上传
     */
    public UploadPretreatment of(InputStream inputStream, String filename, String contentType) {
        return createPretreatment(inputStream, filename, contentType);
    }

    /**
     * 任意对象上传
     */
    public UploadPretreatment of(Object obj, String filename, String contentType) {
        return createPretreatment(obj, filename, contentType);
    }

    /**
     * 任意对象智能识别
     */
    public UploadPretreatment of(Object obj) {
        return createPretreatment(obj, null, null);
    }

    /**
     * 创建预处理
     *
     * @param file     文件
     * @param platform 站台
     * @return {@link UploadPretreatment }
     */
    private UploadPretreatment createPretreatment(MultipartFile file, String platform) {
        UploadPretreatment pretreatment = new UploadPretreatment(this, file);
        return platform != null ? pretreatment.platform(platform) : pretreatment;
    }

    /**
     * 创建预处理
     *
     * @param source      来源
     * @param filename    文件名
     * @param contentType 内容类型
     * @return {@link UploadPretreatment }
     */
    private UploadPretreatment createPretreatment(Object source, String filename, String contentType) {
        FileWrapper wrapper = (filename != null || contentType != null)
            ? FileWrapper.of(source, filename, contentType)
            : FileWrapper.of(source);

        return createPretreatment(wrapper.toMultipartFile(), null);
    }

    /**
     * 添加处理器
     */
    public void addProcessor(FileProcessor processor) {
        tempProcessors.get().add(processor);
    }

    /**
     * 设置进度监听器
     */
    public void onProgress(UploadProgressListener listener) {
        progressListener.set(listener);
    }

    /**
     * 执行上传
     */
    public FileInfo upload(UploadContext context) {
        String platform = context.getPlatform();
        try {
            // 初始化处理器和监听器
            List<FileProcessor> customProcessors = tempProcessors.get();
            UploadProgressListener listener = progressListener.get();

            // 设置进度监听器到上下文
            if (listener != null) {
                context.setProgressListener(listener);
            }

            // 包装文件
            prepareFile(context, listener);

            // 1. 执行文件验证（验证阶段）
            setFileReadPhase(context.getFile(), ProgressAwareMultipartFile.ReadPhase.VALIDATION);
            executeValidation(context, platform, customProcessors);

            // 2. 处理文件名和路径生成
            generateFileNameIfEmpty(context, platform, customProcessors);
            generateFilePathIfEmpty(context, platform, customProcessors);

            // 3. 设置默认bucket
            if (StrUtil.isBlank(context.getBucket())) {
                context.setBucket(getDefaultBucket(platform));
            }

            // 4. 准备缩略图处理
            ThumbnailProcessor thumbnailProcessor = prepareThumbnail(context, platform, customProcessors);

            // 5. 执行实际上传
            setFileReadPhase(context.getFile(), ProgressAwareMultipartFile.ReadPhase.UPLOAD);
            upload(platform, context.getBucket(), context.getFullPath(), context.getFile());

            // 6. 构建文件信息
            FileInfo fileInfo = buildFileInfo(platform, context);

            // 7. 处理缩略图生成（缩略图阶段）
            if (thumbnailProcessor != null && context.isGenerateThumbnail()) {
                setFileReadPhase(context.getFile(), ProgressAwareMultipartFile.ReadPhase.THUMBNAIL);
                processThumbnail(fileInfo, thumbnailProcessor, context);
            }

            // 8. 保存文件记录
            if (fileRecorder != null) {
                fileRecorder.save(fileInfo);
            }

            // 9. 触发完成事件
            triggerCompleteEvent(fileInfo, context, platform, customProcessors);

            return fileInfo;

        } finally {
            cleanup(context);
        }
    }

    /**
     * 准备文件
     */
    private void prepareFile(UploadContext context, UploadProgressListener listener) {
        MultipartFile file = context.getFile();

        // 如果已经是 ProgressAwareMultipartFile，不重复包装
        if (file instanceof ProgressAwareMultipartFile) {
            return;
        }

        // 判断是否需要缓存
        boolean needCache = true;

        // 如果有进度监听器，使用 ProgressAwareMultipartFile
        if (listener != null) {
            context.setFile(new ProgressAwareMultipartFile(file, needCache, listener));
        } else if (!(file instanceof EnhancedMultipartFile)) {
            // 否则只使用 EnhancedMultipartFile 提供缓存
            context.setFile(EnhancedMultipartFile.wrap(file, needCache));
        }
    }

    /**
     * 设置文件读取阶段
     */
    private void setFileReadPhase(MultipartFile file, ProgressAwareMultipartFile.ReadPhase phase) {
        if (file instanceof ProgressAwareMultipartFile) {
            ((ProgressAwareMultipartFile)file).setReadPhase(phase);
        }
    }

    /**
     * 执行文件验证
     */
    private void executeValidation(UploadContext context, String platform, List<FileProcessor> customProcessors) {
        List<FileValidator> validators = collectProcessors(customProcessors, FileValidator.class, platform, context);

        for (FileValidator validator : validators) {
            if (validator.support(context)) {
                validator.validate(context);
            }
        }
    }

    /**
     * 仅在文件名为空时生成文件名
     */
    private void generateFileNameIfEmpty(UploadContext context, String platform, List<FileProcessor> customProcessors) {
        // 如果已有文件名，直接返回
        if (StrUtil.isNotBlank(context.getFormatFileName())) {
            return;
        }

        FileNameGenerator nameGenerator = findFirstProcessor(customProcessors, FileNameGenerator.class, platform, context);

        if (nameGenerator != null && nameGenerator.support(context)) {
            context.setFormatFileName(nameGenerator.generate(context));
        }
    }

    /**
     * 仅在路径为空时生成路径
     */
    private void generateFilePathIfEmpty(UploadContext context, String platform, List<FileProcessor> customProcessors) {
        // 如果已有路径，直接返回
        if (StrUtil.isNotBlank(context.getPath())) {
            return;
        }

        FilePathGenerator pathGenerator = findFirstProcessor(customProcessors, FilePathGenerator.class, platform, context);

        if (pathGenerator != null && pathGenerator.support(context)) {
            context.setPath(pathGenerator.path(context));
        }
    }

    /**
     * 准备缩略图处理
     */
    private ThumbnailProcessor prepareThumbnail(UploadContext context,
                                                String platform,
                                                List<FileProcessor> customProcessors) {
        ThumbnailProcessor thumbnailProcessor = findFirstProcessor(customProcessors, ThumbnailProcessor.class, platform, context);

        boolean needThumbnail = thumbnailProcessor != null && thumbnailProcessor.support(context);
        context.setGenerateThumbnail(needThumbnail);

        return thumbnailProcessor;
    }

    /**
     * 构建文件信息
     */
    private FileInfo buildFileInfo(String platform, UploadContext context) {
        FileInfo fileInfo = getFileInfo(platform, context.getBucket(), context.getFullPath());
        fileInfo.setOriginalFileName(context.getFile().getOriginalFilename());

        if (context.getMetadata() != null && !context.getMetadata().isEmpty()) {
            fileInfo.getMetadata().putAll(context.getMetadata());
        }

        return fileInfo;
    }

    /**
     * 触发上传完成事件
     */
    private void triggerCompleteEvent(FileInfo fileInfo,
                                      UploadContext context,
                                      String platform,
                                      List<FileProcessor> customProcessors) {
        List<UploadCompleteProcessor> completeProcessors = collectProcessors(customProcessors, UploadCompleteProcessor.class, platform, context);

        for (UploadCompleteProcessor processor : completeProcessors) {
            if (processor.support(context)) {
                processor.onComplete(fileInfo);
            }
        }
    }

    /**
     * 收集指定类型的处理器
     */
    private <T extends FileProcessor> List<T> collectProcessors(List<FileProcessor> customProcessors,
                                                                Class<T> processorClass,
                                                                String platform,
                                                                UploadContext context) {

        List<T> processors = new ArrayList<>();

        // 添加自定义处理器
        if (customProcessors != null) {
            customProcessors.stream()
                .filter(processorClass::isInstance)
                .map(processorClass::cast)
                .forEach(processors::add);
        }

        // 添加注册的处理器
        processors.addAll(processorRegistry.getProcessors(processorClass, platform, context));

        return processors;
    }

    /**
     * 查找第一个匹配的处理器
     */
    private <T extends FileProcessor> T findFirstProcessor(List<FileProcessor> customProcessors,
                                                           Class<T> processorClass,
                                                           String platform,
                                                           UploadContext context) {

        // 优先从自定义处理器中查找
        if (customProcessors != null) {
            Optional<T> customProcessor = customProcessors.stream()
                .filter(processorClass::isInstance)
                .map(processorClass::cast)
                .findFirst();

            if (customProcessor.isPresent()) {
                return customProcessor.get();
            }
        }

        // 从注册的处理器中获取
        return processorRegistry.getProcessor(processorClass, platform, context);
    }

    /**
     * 处理缩略图
     */
    private void processThumbnail(FileInfo fileInfo, ThumbnailProcessor processor, UploadContext context) {
        try {
            MultipartFile file = context.getFile();

            // 缩略图生成使用普通流
            try (InputStream is = file.getInputStream()) {
                ThumbnailInfo thumbnailInfo = processor.process(context, is);

                // 生成缩略图路径
                String filePrefix = StrUtil.subBefore(fileInfo.getPath(), StringConstants.DOT, true);
                String thumbnailPath = filePrefix + StorageConstant.THUMBNAIL_SUFFIX + thumbnailInfo.getFormat();
                String thumbnailFileName = StrUtil.subAfter(thumbnailPath, StringConstants.SLASH, true);

                // 创建缩略图文件
                EnhancedMultipartFile thumbnailFile = new EnhancedMultipartFile(thumbnailFileName, thumbnailFileName, StorageConstant.CONTENT_TYPE_IMAGE + thumbnailInfo
                    .getFormat(), thumbnailInfo.getData());

                // 上传缩略图
                upload(context.getPlatform(), context.getBucket(), thumbnailPath, thumbnailFile);

                fileInfo.setThumbnailPath(thumbnailPath);
                fileInfo.setThumbnailSize((long)thumbnailInfo.getData().length);
            }
        } catch (Exception e) {
            log.warn("缩略图处理失败: {}", e.getMessage());
        }
    }

    /**
     * 清理资源
     */
    private void cleanup(UploadContext context) {
        // 清理临时处理器和监听器
        tempProcessors.remove();
        progressListener.remove();

        // 清理文件缓存
        cleanupFileCache(context.getFile());
    }

    /**
     * 清理文件缓存
     */
    private void cleanupFileCache(MultipartFile file) {
        if (file instanceof ProgressAwareMultipartFile) {
            ((ProgressAwareMultipartFile)file).clearCache();
        } else if (file instanceof EnhancedMultipartFile) {
            ((EnhancedMultipartFile)file).clearCache();
        }
    }

    /**
     * 初始化分片上传
     */
    public MultipartInitResp initMultipartUpload(String bucket,
                                                 String platform,
                                                 String path,
                                                 String contentType,
                                                 Map<String, String> metadata) {
        bucket = bucket == null ? getDefaultBucket(platform) : bucket;
        MultipartInitResp result = router.route(platform).initMultipartUpload(bucket, path, contentType, metadata);

        // 记录文件信息
        if (fileRecorder != null) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(result.getFileId());
            fileInfo.setPlatform(platform);
            fileInfo.setBucket(bucket);
            fileInfo.setPath(path);
            fileInfo.setContentType(contentType);
            fileInfo.setMetadata(metadata != null ? new HashMap<>(metadata) : new HashMap<>());
            fileInfo.getMetadata().put("uploadId", result.getUploadId());
            fileInfo.getMetadata().put("status", "UPLOADING");
            fileRecorder.save(fileInfo);
        }
        return result;
    }

    /**
     * 上传分片
     */
    public MultipartUploadResp uploadPart(String platform,
                                          String bucket,
                                          String path,
                                          String uploadId,
                                          int partNumber,
                                          InputStream data) {
        MultipartUploadResp result = router.route(platform).uploadPart(bucket, path, uploadId, partNumber, data);

        // 记录分片信息
        if (fileRecorder != null && result.isSuccess()) {
            FilePartInfo partInfo = new FilePartInfo();
            partInfo.setUploadId(uploadId);
            partInfo.setBucket(bucket);
            partInfo.setPath(path);
            partInfo.setPartNumber(partNumber);
            partInfo.setPartETag(result.getPartETag());
            partInfo.setPartSize(result.getPartSize());
            partInfo.setStatus("SUCCESS");
            partInfo.setUploadTime(LocalDateTime.now());
            fileRecorder.saveFilePart(partInfo);
        }

        return result;
    }

    /**
     * 完成分片上传
     */
    public FileInfo completeMultipartUpload(String platform,
                                            String bucket,
                                            String path,
                                            String uploadId,
                                            List<MultipartUploadResp> clientParts) {
        // 从 FileRecorder 获取所有分片信息
        List<FilePartInfo> recordedParts = fileRecorder != null
            ? fileRecorder.getFileParts(uploadId)
            : new ArrayList<>();

        // 转换为 MultipartUploadResp
        List<MultipartUploadResp> parts = recordedParts.stream().map(partInfo -> {
            MultipartUploadResp resp = new MultipartUploadResp();
            resp.setPartNumber(partInfo.getPartNumber());
            resp.setPartETag(partInfo.getPartETag());
            resp.setPartSize(partInfo.getPartSize());
            resp.setSuccess("SUCCESS".equals(partInfo.getStatus()));
            return resp;
        }).collect(Collectors.toList());

        // 如果没有记录，使用客户端传入的分片信息
        if (parts.isEmpty() && clientParts != null) {
            parts = clientParts;
        }

        // 验证分片完整性
        validatePartsCompleteness(parts);

        // 获取策略，判断是否需要验证
        boolean needVerify = true;
        // 完成上传
        FileInfo fileInfo = router.route(platform).completeMultipartUpload(bucket, path, uploadId, parts, needVerify);

        // 更新文件记录
        if (fileRecorder != null) {
            fileInfo.getMetadata().put("uploadId", uploadId);
            fileInfo.getMetadata().put("status", "COMPLETED");
            fileRecorder.update(fileInfo);

            // 删除分片记录
            fileRecorder.deleteFileParts(uploadId);
        }

        return fileInfo;
    }

    /**
     * 取消分片上传
     */
    public void abortMultipartUpload(String platform, String bucket, String path, String uploadId) {
        router.route(platform).abortMultipartUpload(bucket, path, uploadId);

        // 删除相关记录
        if (fileRecorder != null) {
            fileRecorder.deleteFileParts(uploadId);
        }
    }

    /**
     * 验证分片完整性
     */
    private void validatePartsCompleteness(List<MultipartUploadResp> parts) {
        if (parts.isEmpty()) {
            throw new StorageException("没有找到任何分片信息");
        }

        // 检查分片编号连续性
        List<Integer> partNumbers = parts.stream().map(MultipartUploadResp::getPartNumber).sorted().toList();

        for (int i = 0; i < partNumbers.size(); i++) {
            if (partNumbers.get(i) != i + 1) {
                throw new StorageException("分片编号不连续，缺失分片: " + (i + 1));
            }
        }

        // 检查是否所有分片都成功
        List<Integer> failedParts = parts.stream()
            .filter(part -> !part.isSuccess())
            .map(MultipartUploadResp::getPartNumber)
            .toList();

        if (!failedParts.isEmpty()) {
            throw new StorageException("存在失败的分片: " + failedParts);
        }
    }

    /**
     * 列出已上传的分片
     */
    public List<MultipartUploadResp> listParts(String platform, String bucket, String path, String uploadId) {
        return router.route(platform).listParts(bucket, path, uploadId);
    }

    /**
     * 获取默认存储桶
     *
     * @param platform 站台
     * @return {@link String }
     */
    public String getDefaultBucket(String platform) {
        return router.route(platform).defaultBucket();
    }

    /**
     * 上传
     *
     * @param platform 平台
     * @param bucket   铲斗
     * @param path     路径
     * @param file     文件
     */
    public void upload(String platform, String bucket, String path, MultipartFile file) {
        router.route(platform).upload(bucket, path, file);
    }

    /**
     * 下载文件
     */
    public InputStream download(String platform, String bucket, String path) {
        return router.route(platform).download(bucket, path);
    }

    /**
     * 使用默认存储下载
     */
    public InputStream download(String bucket, String path) {
        return download(storageProperties.getDefaultPlatform(), bucket, path);
    }

    /**
     * 批量下载
     */
    public InputStream batchDownload(String platform, String bucket, List<String> paths) {
        return router.route(platform).batchDownload(bucket, paths);
    }

    /**
     * 删除文件
     */
    public void delete(String platform, String bucket, String path) {
        router.route(platform).delete(bucket, path);
    }

    /**
     * 删除文件
     *
     * @param info 信息
     */
    public void delete(FileInfo info) {
        router.route(info.getPlatform()).delete(info.getBucket(), info.getFullPath());
    }

    /**
     * 批量删除
     */
    public void batchDelete(String platform, String bucket, List<String> paths) {
        router.route(platform).batchDelete(bucket, paths);
    }

    /**
     * 检查文件是否存在
     */
    public boolean exists(String platform, String bucket, String path) {
        return router.route(platform).exists(bucket, path);
    }

    /**
     * 获取文件信息
     */
    public FileInfo getFileInfo(String platform, String bucket, String path) {
        return router.route(platform).getFileInfo(bucket, path);
    }

    /**
     * 列出文件
     */
    public List<FileInfo> list(String platform, String bucket, String prefix, int maxKeys) {
        return router.route(platform).list(bucket, prefix, maxKeys);
    }

    /**
     * 复制文件
     */
    public void copy(String platform, String sourceBucket, String targetBucket, String sourcePath, String targetPath) {
        router.route(platform).copy(sourceBucket, targetBucket, sourcePath, targetPath);
    }

    /**
     * 移动文件
     */
    public void move(String platform, String sourceBucket, String targetBucket, String sourcePath, String targetPath) {
        router.route(platform).move(sourceBucket, targetBucket, sourcePath, targetPath);
    }

    /**
     * 生成预签名URL
     */
    public String generatePresignedUrl(String platform, String bucket, String path, long expireSeconds) {
        return router.route(platform).generatePresignedUrl(bucket, path, expireSeconds);
    }

    /**
     * 动态注册存储策略
     */
    public <T extends StorageStrategy> void register(T strategy) {
        router.registerDynamic(strategy);
    }

    /**
     * 加载动态默认存储
     *
     * @param platform 站台
     */
    public void defaultStorage(String platform) {
        router.registerDynamicDefaultStorage(platform);
    }

    /**
     * 卸载动态注册的策略
     */
    public boolean unload(String platform) {
        if (!router.isDynamic(platform)) {
            throw new StorageException("只能卸载动态注册的策略: " + platform);
        }
        return router.unloadDynamic(platform);
    }

    /**
     * 获取所有可用策略代码
     */
    public Set<String> getAvailablePlatform() {
        return router.getAllPlatform();
    }

    /**
     * 检查策略是否存在
     */
    public boolean exists(String platform) {
        return router.getAllPlatform().contains(platform);
    }

    /**
     * 检查是否为动态注册的策略
     */
    public boolean isDynamic(String platform) {
        return router.isDynamic(platform);
    }

    /**
     * 检查是否为配置文件策略
     */
    public boolean isFromConfig(String platform) {
        return router.isFromConfig(platform);
    }

    /**
     * 获取策略详细信息
     */
    public Map<String, StrategyStatusResp> getStrategyStatus() {
        return router.getFullStrategyStatus();
    }

    /**
     * 获取当前生效的策略信息
     */
    public Map<String, String> getActiveStrategyInfo() {
        return router.getActiveStrategyInfo();
    }
}