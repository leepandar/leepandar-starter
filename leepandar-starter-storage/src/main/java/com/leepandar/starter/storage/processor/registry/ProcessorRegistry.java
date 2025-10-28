package com.leepandar.starter.storage.processor.registry;

import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.processor.preprocess.*;
import com.leepandar.starter.storage.service.FileProcessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 全局处理器注册表
 */
public class ProcessorRegistry {

    private final Map<Class<?>, List<FileProcessor>> processors = new ConcurrentHashMap<>();
    private final Map<String, Map<Class<?>, List<FileProcessor>>> platformProcessors = new ConcurrentHashMap<>();

    /**
     * 注册处理器（自动识别类型）
     */
    public void register(FileProcessor processor) {
        register(processor, null);
    }

    /**
     * 注册平台特定处理器
     */
    public void register(FileProcessor processor, String platform) {
        Class<?> type = getProcessorType(processor);

        if (platform == null) {
            // 全局处理器
            processors.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(processor);
        } else {
            // 平台特定处理器
            platformProcessors.computeIfAbsent(platform, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(type, k -> new CopyOnWriteArrayList<>())
                .add(processor);
        }
    }

    /**
     * 获取处理器类型
     */
    private Class<?> getProcessorType(FileProcessor processor) {
        if (processor instanceof ThumbnailProcessor)
            return ThumbnailProcessor.class;
        if (processor instanceof FileValidator)
            return FileValidator.class;
        if (processor instanceof FileNameGenerator)
            return FileNameGenerator.class;
        if (processor instanceof FilePathGenerator)
            return FilePathGenerator.class;
        if (processor instanceof UploadCompleteProcessor)
            return UploadCompleteProcessor.class;
        return FileProcessor.class;
    }

    /**
     * 获取指定类型的处理器（支持优先级排序）
     */
    @SuppressWarnings("unchecked")
    public <T extends FileProcessor> List<T> getProcessors(Class<T> type, String platform, UploadContext context) {
        List<T> result = new ArrayList<>();

        // 添加全局处理器
        List<FileProcessor> globalList = processors.get(type);
        if (globalList != null) {
            globalList.stream().filter(p -> p.support(context)).map(p -> (T)p).forEach(result::add);
        }

        // 添加平台特定处理器
        if (platform != null) {
            Map<Class<?>, List<FileProcessor>> platformMap = platformProcessors.get(platform);
            if (platformMap != null) {
                List<FileProcessor> platformList = platformMap.get(type);
                if (platformList != null) {
                    platformList.stream().filter(p -> p.support(context)).map(p -> (T)p).forEach(result::add);
                }
            }
        }

        // 按优先级排序（优先级高的在前）
        result.sort(Comparator.comparingInt(FileProcessor::getOrder).reversed());
        return result;
    }

    /**
     * 获取最高优先级的处理器
     */
    public <T extends FileProcessor> T getProcessor(Class<T> type, String platform, UploadContext context) {
        List<T> processors = getProcessors(type, platform, context);
        return processors.isEmpty() ? null : processors.get(0);
    }
}