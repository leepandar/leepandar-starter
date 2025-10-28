package com.leepandar.starter.storage.domain.model.resp;

/**
 * 分片上传结果
 */
public class MultipartUploadResp {
    /**
     * 分片编号
     */
    private Integer partNumber;

    /**
     * 分片ETag
     */
    private String partETag;

    /**
     * 分片大小
     */
    private Long partSize;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartETag() {
        return partETag;
    }

    public void setPartETag(String partETag) {
        this.partETag = partETag;
    }

    public Long getPartSize() {
        return partSize;
    }

    public void setPartSize(Long partSize) {
        this.partSize = partSize;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}