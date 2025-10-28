package com.leepandar.starter.storage.autoconfigure.properties;

import com.leepandar.starter.storage.common.constant.StorageConstant;

/**
 * oss 存储配置
 */
public class OssStorageConfig {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 存储平台
     */
    private String platform;

    /**
     * 连接地址 带 http://
     */
    private String endpoint;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 默认桶
     */
    private String bucketName;

    /**
     * 区域
     */
    private String region;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 是否启用传输加速
     */
    private boolean transferAccelerationEnabled = false;

    /**
     * 多部分上传阈值（字节）
     */
    private long multipartUploadThreshold = StorageConstant.DEFAULT_FILE_SIZE;

    /**
     * 多部分上传的部分大小（字节）
     */
    private long multipartUploadPartSize = StorageConstant.DEFAULT_FILE_SIZE;

    /**
     * 请求超时时间（秒）
     */
    private int requestTimeout = 30;

    /**
     * 默认的对象ACL
     */
    private String defaultAcl = StorageConstant.DEFAULT_ACL;

    /**
     * 是否启用路径样式访问
     */
    private boolean pathStyleAccessEnabled = false;

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isTransferAccelerationEnabled() {
        return transferAccelerationEnabled;
    }

    public void setTransferAccelerationEnabled(boolean transferAccelerationEnabled) {
        this.transferAccelerationEnabled = transferAccelerationEnabled;
    }

    public long getMultipartUploadThreshold() {
        return multipartUploadThreshold;
    }

    public void setMultipartUploadThreshold(long multipartUploadThreshold) {
        this.multipartUploadThreshold = multipartUploadThreshold;
    }

    public long getMultipartUploadPartSize() {
        return multipartUploadPartSize;
    }

    public void setMultipartUploadPartSize(long multipartUploadPartSize) {
        this.multipartUploadPartSize = multipartUploadPartSize;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getDefaultAcl() {
        return defaultAcl;
    }

    public void setDefaultAcl(String defaultAcl) {
        this.defaultAcl = defaultAcl;
    }

    public boolean isPathStyleAccessEnabled() {
        return pathStyleAccessEnabled;
    }

    public void setPathStyleAccessEnabled(boolean pathStyleAccessEnabled) {
        this.pathStyleAccessEnabled = pathStyleAccessEnabled;
    }
}
