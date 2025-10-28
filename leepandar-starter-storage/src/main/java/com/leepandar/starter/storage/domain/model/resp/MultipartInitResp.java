package com.leepandar.starter.storage.domain.model.resp;

/**
 * 分片上传初始化结果
 */
public class MultipartInitResp {
    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 上传ID（S3返回的uploadId）
     */
    private String uploadId;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 存储平台
     */
    private String platform;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 分片大小
     */
    private Long partSize;

    /**
     * 总分片数
     */
    private Integer totalParts;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getPartSize() {
        return partSize;
    }

    public void setPartSize(Long partSize) {
        this.partSize = partSize;
    }

    public Integer getTotalParts() {
        return totalParts;
    }

    public void setTotalParts(Integer totalParts) {
        this.totalParts = totalParts;
    }
}