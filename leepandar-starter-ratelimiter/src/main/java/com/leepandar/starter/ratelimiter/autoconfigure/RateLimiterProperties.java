package com.leepandar.starter.ratelimiter.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.leepandar.starter.core.constant.PropertiesConstants;

/**
 * 限流器配置属性
 */
@ConfigurationProperties(PropertiesConstants.RATE_LIMITER)
public class RateLimiterProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * Key 前缀
     */
    private String keyPrefix = "RateLimiter";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
