package com.leepandar.starter.storage.processor.preprocess.impl;

import cn.hutool.core.util.StrUtil;
import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.processor.preprocess.FileNameGenerator;
import com.leepandar.starter.storage.common.util.StorageUtils;

/**
 * 默认文件名生成器
 */
public class DefaultFileNameGenerator implements FileNameGenerator {

    @Override
    public String getName() {
        return DefaultFilePathGenerator.class.getSimpleName();
    }

    @Override
    public boolean support(UploadContext context) {
        return StrUtil.isBlank(context.getFormatFileName());
    }

    @Override
    public String generate(UploadContext context) {
        return StorageUtils.generateFileName(context.getFile().getOriginalFilename());
    }
}