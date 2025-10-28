package com.leepandar.starter.storage.processor.preprocess.impl;

import cn.hutool.core.util.StrUtil;
import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.processor.preprocess.FilePathGenerator;
import com.leepandar.starter.storage.common.util.StorageUtils;

/**
 * 默认文件路径生成器
 */
public class DefaultFilePathGenerator implements FilePathGenerator {

    @Override
    public String getName() {
        return DefaultFilePathGenerator.class.getSimpleName();
    }

    @Override
    public boolean support(UploadContext context) {
        return StrUtil.isBlank(context.getPath());
    }

    @Override
    public String path(UploadContext context) {
        String path = context.getPath();
        return StrUtil.isNotBlank(path) ? path : StorageUtils.generatePath();
    }
}
