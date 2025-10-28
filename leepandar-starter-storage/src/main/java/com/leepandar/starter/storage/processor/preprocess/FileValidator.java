package com.leepandar.starter.storage.processor.preprocess;

import com.leepandar.starter.storage.common.exception.StorageException;
import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.service.FileProcessor;

/**
 * 文件验证器
 */
public interface FileValidator extends FileProcessor {

    /**
     * 验证文件
     * 
     * @param context 上传上下文
     * @throws StorageException 验证失败时抛出异常
     */
    void validate(UploadContext context) throws StorageException;
}