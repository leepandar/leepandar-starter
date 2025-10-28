package com.leepandar.starter.storage.domain.model.resp;

import java.time.LocalDateTime;

/**
 * 文件分片信息
 */
public class FilePartInfo {
    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 分片编号（从1开始）
     */
    private Integer partNumber;

    /**
     * 分片大小
     */
    private Long partSize;

    /**
     * 分片MD5
     */
    private String partMd5;

    /**
     * 分片ETag（S3返回的标识）
     */
    private String partETag;

    /**
     * 上传ID（S3分片上传标识）
     */
    private String uploadId;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 状态：UPLOADING, SUCCESS, FAILED
     */
    private String status;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 文件路径
     */
    private String path;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public Long getPartSize() {
        return partSize;
    }

    public void setPartSize(Long partSize) {
        this.partSize = partSize;
    }

    public String getPartMd5() {
        return partMd5;
    }

    public void setPartMd5(String partMd5) {
        this.partMd5 = partMd5;
    }

    public String getPartETag() {
        return partETag;
    }

    public void setPartETag(String partETag) {
        this.partETag = partETag;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}