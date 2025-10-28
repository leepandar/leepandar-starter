package com.leepandar.starter.storage.processor.preprocess.impl;

import cn.hutool.core.io.FileUtil;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import com.leepandar.starter.storage.common.constant.StorageConstant;
import com.leepandar.starter.storage.common.exception.StorageException;
import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.processor.preprocess.FileValidator;

/**
 * 文件大小验证器
 */
public class FileSizeValidator implements FileValidator {

    private final long maxSize;

    public FileSizeValidator(MultipartProperties multipartProperties) {
        this.maxSize = multipartProperties.getMaxFileSize().toBytes();
    }

    public FileSizeValidator() {
        this(StorageConstant.DEFAULT_FILE_SIZE);
    }

    public FileSizeValidator(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public String getName() {
        return FileSizeValidator.class.getSimpleName();
    }

    @Override
    public boolean support(UploadContext context) {
        return true;
    }

    @Override
    public void validate(UploadContext context) throws StorageException {
        long fileSize = context.getFile().getSize();
        if (fileSize > maxSize) {
            throw new StorageException(String.format("文件大小超过限制: %s (当前: %s)", FileUtil
                .readableFileSize(maxSize), FileUtil.readableFileSize(fileSize)));
        }
    }

    /**
     * 创建默认的文件大小验证器（10MB）
     */
    public FileSizeValidator create() {
        return new FileSizeValidator(maxSize);
    }

    /**
     * 创建指定大小的验证器
     */
    public static FileSizeValidator maxSize(long bytes) {
        return new FileSizeValidator(bytes);
    }

    /**
     * 创建指定KB大小的验证器
     */
    public static FileSizeValidator maxKB(long kb) {
        return new FileSizeValidator(kb * 1024);
    }

    /**
     * 创建指定MB大小的验证器
     */
    public static FileSizeValidator maxMB(long mb) {
        return new FileSizeValidator(mb * 1024 * 1024);
    }

    /**
     * 创建指定GB大小的验证器
     */
    public static FileSizeValidator maxGB(long gb) {
        return new FileSizeValidator(gb * 1024L * 1024 * 1024);
    }
}
