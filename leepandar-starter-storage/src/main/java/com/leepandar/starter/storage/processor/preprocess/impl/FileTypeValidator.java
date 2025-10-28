package com.leepandar.starter.storage.processor.preprocess.impl;

import cn.hutool.core.io.FileUtil;
import com.leepandar.starter.storage.common.exception.StorageException;
import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.processor.preprocess.FileValidator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件类型验证器
 */
public class FileTypeValidator implements FileValidator {

    private final Set<String> allowedExtensions;

    /**
     * 默认构造函数，允许所有支持的文件类型
     */
    public FileTypeValidator() {
        this.allowedExtensions = null;
    }

    /**
     * 指定允许的文件扩展名
     *
     * @param extensions 允许的扩展名
     */
    public FileTypeValidator(String... extensions) {
        this.allowedExtensions = new HashSet<>(Arrays.asList(extensions));
    }

    @Override
    public String getName() {
        return FileTypeValidator.class.getSimpleName();
    }

    @Override
    public boolean support(UploadContext context) {
        return true;
    }

    @Override
    public void validate(UploadContext context) throws StorageException {
        String filename = context.getFile().getOriginalFilename();
        if (filename != null) {
            String extension = FileUtil.extName(filename).toLowerCase();
            // 如果指定了允许的扩展名，则只允许这些扩展名
            if (allowedExtensions != null) {
                if (!allowedExtensions.contains(extension)) {
                    throw new StorageException("不支持的文件类型: " + extension + "，仅支持: " + String
                        .join(", ", allowedExtensions));
                }
            }
        }
    }
}