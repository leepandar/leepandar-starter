package com.leepandar.starter.log.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.log.enums.Include;
import com.leepandar.starter.core.util.SpringWebUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 日志配置属性
 */
@ConfigurationProperties(PropertiesConstants.LOG)
public class LogProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 访问日志配置
     */
    @NestedConfigurationProperty
    private AccessLogProperties accessLog = new AccessLogProperties();

    /**
     * 包含信息
     */
    private Set<Include> includes = Include.defaultIncludes();

    /**
     * 放行路由
     */
    private List<String> excludePatterns = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Include> getIncludes() {
        return includes;
    }

    public void setIncludes(Set<Include> includes) {
        this.includes = includes;
    }

    public List<String> getExcludePatterns() {
        return excludePatterns;
    }

    public void setExcludePatterns(List<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    public AccessLogProperties getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(AccessLogProperties accessLog) {
        this.accessLog = accessLog;
    }

    /**
     * 是否匹配放行路由
     *
     * @param uri 请求 URI
     * @return 是否匹配
     */
    public boolean isMatch(String uri) {
        return this.getExcludePatterns().stream().anyMatch(pattern -> SpringWebUtils.isMatch(uri, pattern));
    }
}
