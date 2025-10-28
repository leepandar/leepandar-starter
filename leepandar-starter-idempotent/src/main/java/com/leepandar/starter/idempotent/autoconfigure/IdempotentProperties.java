package com.leepandar.starter.idempotent.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.leepandar.starter.core.constant.PropertiesConstants;

/**
 * 幂等配置属性
 */
@ConfigurationProperties(PropertiesConstants.IDEMPOTENT)
public class IdempotentProperties {

    /**
     * Key 前缀
     */
    private String keyPrefix = "Idempotent";

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
