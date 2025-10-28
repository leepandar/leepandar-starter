package com.leepandar.starter.storage.autoconfigure;

import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.storage.autoconfigure.properties.OssStorageConfig;
import com.leepandar.starter.storage.autoconfigure.properties.StorageProperties;
import com.leepandar.starter.storage.engine.StorageStrategyRegistrar;
import com.leepandar.starter.storage.strategy.StorageStrategy;
import com.leepandar.starter.storage.strategy.impl.OssStorageStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * s3存储自动配置
 */
@ConditionalOnProperty(prefix = PropertiesConstants.STORAGE, name = "oss")
public class OssStorageAutoConfiguration implements StorageStrategyRegistrar {

    private final StorageProperties properties;

    public OssStorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    /**
     * 注册 OSS 存储策略
     *
     * @param strategies 策略列表
     */
    @Override
    @Bean
    public void register(List<StorageStrategy> strategies) {
        for (OssStorageConfig config : properties.getOss()) {
            if (config.isEnabled()) {
                strategies.add(new OssStorageStrategy(config));
            }
        }
    }
}
