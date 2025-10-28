package com.leepandar.starter.auth.satoken.enums;

/**
 * SaToken 持久层类型枚举
 */
public enum SaTokenDaoType {

    /**
     * 默认（内存）
     */
    DEFAULT,

    /**
     * Redis
     */
    REDIS,

    /**
     * 自定义
     */
    CUSTOM
}
