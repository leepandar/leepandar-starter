package com.leepandar.starter.storage.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import com.leepandar.starter.storage.annotation.PlatformProcessor;
import com.leepandar.starter.storage.autoconfigure.properties.StorageProperties;
import com.leepandar.starter.storage.core.FileStorageService;
import com.leepandar.starter.storage.engine.StorageDecoratorManager;
import com.leepandar.starter.storage.processor.registry.ProcessorRegistry;
import com.leepandar.starter.storage.processor.preprocess.*;
import com.leepandar.starter.storage.processor.preprocess.impl.*;
import com.leepandar.starter.storage.engine.StorageStrategyRegistrar;
import com.leepandar.starter.storage.engine.StorageStrategyRouter;
import com.leepandar.starter.storage.service.FileProcessor;
import com.leepandar.starter.storage.service.FileRecorder;
import com.leepandar.starter.storage.service.impl.DefaultFileRecorder;

import java.util.List;
import java.util.Map;

/**
 * 存储自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
@Import({ProcessorRegistry.class})
public class StorageAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(StorageAutoConfiguration.class);

    private final StorageProperties properties;
    private final ApplicationContext applicationContext;

    public StorageAutoConfiguration(StorageProperties properties, ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    /**
     * 策略路由器
     *
     * @param registrars 注册
     * @return {@link StorageStrategyRouter }
     */
    @Bean
    public StorageStrategyRouter strategyRouter(List<StorageStrategyRegistrar> registrars) {
        return new StorageStrategyRouter(registrars, properties, storageDecoratorManager());
    }

    /**
     * 存储装饰器管理器
     *
     * @return {@link StorageDecoratorManager }
     */
    @Bean
    public StorageDecoratorManager storageDecoratorManager() {
        return new StorageDecoratorManager(applicationContext);
    }

    /**
     * oss存储自动配置
     *
     * @return {@link OssStorageAutoConfiguration }
     */
    @Bean
    public OssStorageAutoConfiguration ossStorageAutoConfiguration() {
        return new OssStorageAutoConfiguration(properties);
    }

    /**
     * 本地存储自动配置
     *
     * @return {@link LocalStorageAutoConfiguration }
     */
    @Bean
    public LocalStorageAutoConfiguration localStorageAutoConfiguration() {
        return new LocalStorageAutoConfiguration(properties);
    }

    /**
     * 文件存储服务
     *
     * @param router            路由
     * @param storageProperties 存储属性
     * @param processorRegistry 处理器注册表
     * @param fileRecorder      文件记录器
     * @return {@link FileStorageService }
     */
    @Bean
    public FileStorageService fileStorageService(StorageStrategyRouter router,
                                                 StorageProperties storageProperties,
                                                 ProcessorRegistry processorRegistry,
                                                 FileRecorder fileRecorder) {
        return new FileStorageService(router, storageProperties, processorRegistry, fileRecorder);
    }

    /**
     * 文件记录器
     *
     * @return {@link FileRecorder }
     */
    @Bean
    @ConditionalOnMissingBean
    public FileRecorder fileRecorder() {
        return new DefaultFileRecorder();
    }

    /**
     * 处理器注册中心
     */
    @Bean
    public ProcessorRegistry processorRegistry() {
        ProcessorRegistry registry = new ProcessorRegistry();

        // 自动发现并注册所有 FileProcessor 实现
        Map<String, FileProcessor> processors = applicationContext.getBeansOfType(FileProcessor.class);
        processors.values().forEach(processor -> {
            // 检查是否有平台注解
            PlatformProcessor annotation = processor.getClass().getAnnotation(PlatformProcessor.class);
            if (annotation != null) {
                for (String platform : annotation.platforms()) {
                    registry.register(processor, platform);
                }
            } else {
                // 注册为全局处理器
                registry.register(processor);
            }
        });
        return registry;
    }

    /**
     * 默认文件名生成器
     */
    @Bean
    @ConditionalOnMissingBean(FileNameGenerator.class)
    public FileNameGenerator defaultFileNameGenerator() {
        return new DefaultFileNameGenerator();
    }

    /**
     * 默认路径生成器
     */
    @Bean
    @ConditionalOnMissingBean(FilePathGenerator.class)
    public FilePathGenerator defaultFilePathGenerator() {
        return new DefaultFilePathGenerator();
    }

    /**
     * 默认缩略图处理器
     */
    @Bean
    @ConditionalOnMissingBean(ThumbnailProcessor.class)
    @ConditionalOnClass(name = "net.coobird.thumbnailator.Thumbnails")
    public ThumbnailProcessor defaultThumbnailProcessor() {
        return new DefaultThumbnailProcessor();
    }

    /**
     * 文件大小验证器
     */
    @Bean
    @ConditionalOnMissingBean(name = "fileSizeValidator")
    public FileValidator fileSizeValidator(MultipartProperties multipartProperties) {
        return new FileSizeValidator(multipartProperties);
    }

    /**
     * 文件类型验证器
     */
    @Bean
    @ConditionalOnMissingBean(name = "fileTypeValidator")
    public FileValidator fileTypeValidator() {
        return new FileTypeValidator();
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'Storage' completed initialization.");
    }
}
