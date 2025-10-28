package com.leepandar.starter.storage.processor.preprocess;

import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.service.FileProcessor;

/**
 * 文件路径生成器
 */
public interface FilePathGenerator extends FileProcessor {

    /**
     * 生成路径
     *
     * @param context 上下文
     * @return {@link String }
     */
    String path(UploadContext context);
}
