package com.leepandar.starter.encrypt.api.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.leepandar.starter.core.constant.PropertiesConstants;

/**
 * API 加密配置属性
 */
@ConfigurationProperties(PropertiesConstants.ENCRYPT_API)
public class ApiEncryptProperties {

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 请求头中 AES 密钥 键名
     */
    private String secretKeyHeader = "X-Api-Encrypt";

    /**
     * 响应加密公钥
     */
    private String publicKey;

    /**
     * 请求解密私钥
     */
    private String privateKey;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecretKeyHeader() {
        return secretKeyHeader;
    }

    public void setSecretKeyHeader(String secretKeyHeader) {
        this.secretKeyHeader = secretKeyHeader;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
