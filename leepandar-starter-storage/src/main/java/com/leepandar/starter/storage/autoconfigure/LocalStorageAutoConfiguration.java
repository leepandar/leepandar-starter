package com.leepandar.starter.storage.autoconfigure;

import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.storage.autoconfigure.properties.LocalStorageConfig;
import com.leepandar.starter.storage.autoconfigure.properties.StorageProperties;
import com.leepandar.starter.storage.engine.StorageStrategyRegistrar;
import com.leepandar.starter.storage.strategy.StorageStrategy;
import com.leepandar.starter.storage.strategy.impl.LocalStorageStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 本地存储自动配置
 */
@ConditionalOnProperty(prefix = PropertiesConstants.STORAGE, name = "local")
public class LocalStorageAutoConfiguration implements StorageStrategyRegistrar {

    private final StorageProperties storageProperties;

    public LocalStorageAutoConfiguration(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    /**
     * 注册本地存储策略
     *
     * @param strategies 策略列表
     */
    @Bean
    @Override
    public void register(List<StorageStrategy> strategies) {
        for (LocalStorageConfig config : storageProperties.getLocal()) {
            if (config.isEnabled()) {
                strategies.add(new LocalStorageStrategy(config));
            }
        }
    }
}
