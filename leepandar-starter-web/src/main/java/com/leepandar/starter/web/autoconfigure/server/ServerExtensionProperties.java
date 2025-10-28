package com.leepandar.starter.web.autoconfigure.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器配置属性
 */
@ConfigurationProperties("server.extension")
public class ServerExtensionProperties {

    /**
     * 默认禁止三个不安全的 HTTP 方法（如 CONNECT、TRACE、TRACK）
     */
    private static final List<String> DEFAULT_ALLOWED_METHODS = List.of("CONNECT", "TRACE", "TRACK");

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 不允许的请求方式
     */
    private List<String> disallowedMethods = new ArrayList<>(DEFAULT_ALLOWED_METHODS);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getDisallowedMethods() {
        return disallowedMethods;
    }

    public void setDisallowedMethods(List<String> disallowedMethods) {
        this.disallowedMethods = disallowedMethods;
    }
}
