package com.leepandar.starter.storage.service;

import com.leepandar.starter.storage.domain.model.context.UploadContext;

/**
 * 文件处理器接口
 */
public interface FileProcessor {

    /**
     * 获取处理器名称
     * 
     * @return 处理器名称
     */
    String getName();

    /**
     * 获取处理器优先级（数值越大优先级越高）
     * 
     * @return 优先级
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 是否支持该文件
     * 
     * @param context 上传上下文
     * @return 是否支持
     */
    boolean support(UploadContext context);
}