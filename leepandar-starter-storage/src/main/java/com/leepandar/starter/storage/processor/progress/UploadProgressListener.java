package com.leepandar.starter.storage.processor.progress;

/**
 * 上传进度监听器
 **/
public interface UploadProgressListener {

    /**
     * 进度更新回调
     *
     * @param bytesRead  已读取字节数
     * @param totalBytes 总字节数（-1表示未知）
     * @param percentage 百分比（0-100）
     */
    void onProgress(long bytesRead, long totalBytes, int percentage);

    /**
     * 上传开始
     */
    default void onStart() {
    }

    /**
     * 上传完成
     */
    default void onComplete() {
    }

    /**
     * 上传失败
     */
    default void onError(Exception e) {
    }
}
