package com.leepandar.starter.datapermission.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ResolvableType;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.datapermission.provider.DataPermissionUserDataProvider;
import com.leepandar.starter.datapermission.handler.DefaultDataPermissionHandler;

/**
 * 数据权限自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(DataPermissionProperties.class)
@ConditionalOnProperty(prefix = PropertiesConstants.DATA_PERMISSION, name = PropertiesConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public class DataPermissionAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DataPermissionAutoConfiguration.class);

    private DataPermissionAutoConfiguration() {
    }

    /**
     * 数据权限拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionHandler dataPermissionHandler) {
        return new DataPermissionInterceptor(dataPermissionHandler);
    }

    /**
     * 数据权限处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public DataPermissionHandler dataPermissionHandler(DataPermissionUserDataProvider dataPermissionUserDataProvider) {
        return new DefaultDataPermissionHandler(dataPermissionUserDataProvider);
    }

    /**
     * 数据权限用户数据提供者
     */
    @Bean
    @ConditionalOnMissingBean
    public DataPermissionUserDataProvider dataPermissionUserDataProvider() {
        if (log.isErrorEnabled()) {
            log.error("Consider defining a bean of type '{}' in your configuration.", ResolvableType
                .forClass(DataPermissionUserDataProvider.class));
        }
        throw new NoSuchBeanDefinitionException(DataPermissionUserDataProvider.class);
    }

    static {
        log.debug("Auto Configuration 'DataPermission' completed initialization.");
    }
}
