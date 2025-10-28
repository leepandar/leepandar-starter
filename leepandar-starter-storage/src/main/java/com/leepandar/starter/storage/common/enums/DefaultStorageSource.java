package com.leepandar.starter.storage.common.enums;

/**
 * 默认存储配置来源
 * 决定默认存储平台配置从哪里加载
 */
public enum DefaultStorageSource {

    /**
     * 从配置文件加载默认存储
     */
    CONFIG,

    /**
     * 从动态配置加载默认存储
     */
    DYNAMIC,

}
