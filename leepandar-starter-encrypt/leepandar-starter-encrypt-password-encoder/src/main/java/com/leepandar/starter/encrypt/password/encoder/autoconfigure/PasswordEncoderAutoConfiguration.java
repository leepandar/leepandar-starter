package com.leepandar.starter.encrypt.password.encoder.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.core.util.validation.CheckUtils;
import com.leepandar.starter.encrypt.password.encoder.enums.PasswordEncoderAlgorithm;
import com.leepandar.starter.encrypt.password.encoder.util.PasswordEncoderUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码编码器自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(PasswordEncoderProperties.class)
@ConditionalOnProperty(prefix = PropertiesConstants.ENCRYPT_PASSWORD_ENCODER, name = PropertiesConstants.ENABLED, havingValue = "true")
public class PasswordEncoderAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncoderAutoConfiguration.class);

    /**
     * 密码编码器配置
     *
     * @see DelegatingPasswordEncoder
     * @see PasswordEncoderFactories
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder(PasswordEncoderProperties properties) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(PasswordEncoderAlgorithm.BCRYPT.name().toLowerCase(), PasswordEncoderUtil
            .getEncoder(PasswordEncoderAlgorithm.BCRYPT));
        encoders.put(PasswordEncoderAlgorithm.SCRYPT.name().toLowerCase(), PasswordEncoderUtil
            .getEncoder(PasswordEncoderAlgorithm.SCRYPT));
        encoders.put(PasswordEncoderAlgorithm.PBKDF2.name().toLowerCase(), PasswordEncoderUtil
            .getEncoder(PasswordEncoderAlgorithm.PBKDF2));
        encoders.put(PasswordEncoderAlgorithm.ARGON2.name().toLowerCase(), PasswordEncoderUtil
            .getEncoder(PasswordEncoderAlgorithm.ARGON2));
        PasswordEncoderAlgorithm algorithm = properties.getAlgorithm();
        CheckUtils.throwIf(PasswordEncoderUtil.getEncoder(algorithm) == null, "不支持的加密算法: {}", algorithm);
        return new DelegatingPasswordEncoder(algorithm.name().toLowerCase(), encoders);
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'Encrypt-Password Encoder' completed initialization.");
    }
}
