package com.leepandar.starter.datapermission.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.leepandar.starter.core.constant.PropertiesConstants;

/**
 * 数据权限配置属性
 */
@ConfigurationProperties(PropertiesConstants.DATA_PERMISSION)
public class DataPermissionProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
