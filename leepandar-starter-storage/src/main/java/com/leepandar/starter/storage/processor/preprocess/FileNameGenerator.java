package com.leepandar.starter.storage.processor.preprocess;

import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.service.FileProcessor;

/**
 * 文件名生成器
 */
public interface FileNameGenerator extends FileProcessor {

    /**
     * 生成文件名
     * 
     * @param context 上传上下文
     * @return 生成的文件名
     */
    String generate(UploadContext context);
}
