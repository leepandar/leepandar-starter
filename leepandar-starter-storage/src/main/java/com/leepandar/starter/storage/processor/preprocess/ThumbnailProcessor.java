package com.leepandar.starter.storage.processor.preprocess;

import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.domain.model.req.ThumbnailInfo;
import com.leepandar.starter.storage.service.FileProcessor;

import java.io.InputStream;

/**
 * 缩略图处理器
 */
public interface ThumbnailProcessor extends FileProcessor {

    /**
     * 生成缩略图
     * 
     * @param context           上传上下文
     * @param sourceInputStream 原始文件流
     * @return 缩略图信息
     */
    ThumbnailInfo process(UploadContext context, InputStream sourceInputStream);
}