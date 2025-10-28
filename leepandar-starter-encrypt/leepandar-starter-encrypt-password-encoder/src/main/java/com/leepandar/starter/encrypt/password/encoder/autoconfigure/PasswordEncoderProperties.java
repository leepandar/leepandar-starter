package com.leepandar.starter.encrypt.password.encoder.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.encrypt.password.encoder.enums.PasswordEncoderAlgorithm;

/**
 * 密码编码器配置属性
 */
@ConfigurationProperties(PropertiesConstants.ENCRYPT_PASSWORD_ENCODER)
public class PasswordEncoderProperties {

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 默认启用的编码器算法（默认：BCrypt 加密算法）
     */
    private PasswordEncoderAlgorithm algorithm = PasswordEncoderAlgorithm.BCRYPT;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public PasswordEncoderAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(PasswordEncoderAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
}