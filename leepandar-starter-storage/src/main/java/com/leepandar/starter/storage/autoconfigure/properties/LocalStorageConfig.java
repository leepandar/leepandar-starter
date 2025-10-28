package com.leepandar.starter.storage.autoconfigure.properties;

/**
 * 本地存储配置
 */
public class LocalStorageConfig {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 存储平台
     */
    private String platform;

    /**
     * 存储路径
     */
    private String bucketName;

    /**
     * 访问路径
     */
    private String endpoint;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
