package com.leepandar.starter.storage.autoconfigure.properties;

import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.storage.common.constant.StorageConstant;
import com.leepandar.starter.storage.common.enums.DefaultStorageSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储属性
 */
@ConfigurationProperties(prefix = PropertiesConstants.STORAGE)
public class StorageProperties {

    /**
     * 默认使用的存储平台
     */
    private String defaultPlatform = StorageConstant.DEFAULT_STORAGE_PLATFORM;

    /**
     * 默认存储配置来源 (配置文件/动态配置)
     */
    private DefaultStorageSource defaultStorageSource = DefaultStorageSource.DYNAMIC;

    /**
     * 本地存储配置列表
     */
    private List<LocalStorageConfig> local = new ArrayList<>();

    /**
     * oss 存储配置列表
     */
    private List<OssStorageConfig> oss = new ArrayList<>();

    public String getDefaultPlatform() {
        return defaultPlatform;
    }

    public void setDefaultPlatform(String defaultPlatform) {
        this.defaultPlatform = defaultPlatform;
    }

    public DefaultStorageSource getDefaultStorageSource() {
        return defaultStorageSource;
    }

    public void setDefaultStorageSource(DefaultStorageSource defaultStorageSource) {
        this.defaultStorageSource = defaultStorageSource;
    }

    public List<LocalStorageConfig> getLocal() {
        return local;
    }

    public void setLocal(List<LocalStorageConfig> local) {
        this.local = local;
    }

    public List<OssStorageConfig> getOss() {
        return oss;
    }

    public void setOss(List<OssStorageConfig> oss) {
        this.oss = oss;
    }
}
