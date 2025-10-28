package com.leepandar.starter.encrypt.field.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.encrypt.field.interceptor.MyBatisDecryptInterceptor;
import com.leepandar.starter.encrypt.field.interceptor.MyBatisEncryptInterceptor;
import com.leepandar.starter.encrypt.field.util.EncryptHelper;

/**
 * 字段加密自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(FieldEncryptProperties.class)
@ConditionalOnProperty(prefix = PropertiesConstants.ENCRYPT_FIELD, name = PropertiesConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public class FieldEncryptAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(FieldEncryptAutoConfiguration.class);
    private final FieldEncryptProperties properties;

    public FieldEncryptAutoConfiguration(FieldEncryptProperties properties) {
        this.properties = properties;
    }

    /**
     * MyBatis 加密拦截器配置
     */
    @Bean
    @ConditionalOnMissingBean
    public MyBatisEncryptInterceptor mybatisEncryptInterceptor() {
        return new MyBatisEncryptInterceptor();
    }

    /**
     * MyBatis 解密拦截器配置
     */
    @Bean
    @ConditionalOnMissingBean(MyBatisDecryptInterceptor.class)
    public MyBatisDecryptInterceptor mybatisDecryptInterceptor() {
        return new MyBatisDecryptInterceptor();
    }

    @PostConstruct
    public void postConstruct() {
        EncryptHelper.init(properties);
        log.debug("Auto Configuration 'Encrypt-Field' completed initialization.");
    }
}
