package com.leepandar.starter.storage.processor.preprocess;

import com.leepandar.starter.storage.domain.model.resp.FileInfo;
import com.leepandar.starter.storage.service.FileProcessor;

/**
 * 上传完成处理器
 */
public interface UploadCompleteProcessor extends FileProcessor {

    /**
     * 处理上传完成事件
     * 
     * @param fileInfo 文件信息
     */
    void onComplete(FileInfo fileInfo);
}