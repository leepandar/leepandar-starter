package com.leepandar.starter.storage.annotation;

import java.lang.annotation.*;

/**
 * 平台处理器注解
 * 该注解用于标记文件前置处理器类，以指定其适用的平台范围。
 * 主要用于实现平台特定的文件处理逻辑，如文件名生成、路径转换、格式适配等。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PlatformProcessor {

    /**
     * 适用的平台列表
     */
    String[] platforms();
}