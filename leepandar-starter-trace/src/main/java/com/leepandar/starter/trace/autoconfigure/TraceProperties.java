package com.leepandar.starter.trace.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import com.leepandar.starter.core.constant.PropertiesConstants;

/**
 * 链路追踪配置属性
 */
@ConfigurationProperties(PropertiesConstants.TRACE)
public class TraceProperties {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 链路 ID 名称
     */
    private String traceIdName = "traceId";

    /**
     * TLog 配置
     */
    @NestedConfigurationProperty
    private TLogProperties tlog;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTraceIdName() {
        return traceIdName;
    }

    public void setTraceIdName(String traceIdName) {
        this.traceIdName = traceIdName;
    }

    public TLogProperties getTlog() {
        return tlog;
    }

    public void setTlog(TLogProperties tlog) {
        this.tlog = tlog;
    }
}
