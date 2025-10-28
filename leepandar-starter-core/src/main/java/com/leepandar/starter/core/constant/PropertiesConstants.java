package com.leepandar.starter.core.constant;

/**
 * 配置属性相关常量
 */
public class PropertiesConstants {

    /**
     * leepandar Starter
     */
    public static final String LEEPANDAR_STARTER = "leepandar-starter";

    /**
     * 启用配置
     */
    public static final String ENABLED = "enabled";

    /**
     * Web 配置
     */
    public static final String WEB = LEEPANDAR_STARTER + StringConstants.DOT + "web";

    /**
     * Web-跨域配置
     */
    public static final String WEB_CORS = WEB + StringConstants.DOT + "cors";

    /**
     * Web-响应配置
     */
    public static final String WEB_RESPONSE = WEB + StringConstants.DOT + "response";

    /**
     * 加密配置
     */
    public static final String ENCRYPT = LEEPANDAR_STARTER + StringConstants.DOT + "encrypt";

    /**
     * 加密-密码编码器
     */
    public static final String ENCRYPT_PASSWORD_ENCODER = ENCRYPT + StringConstants.DOT + "password-encoder";

    /**
     * 加密-字段加密
     */
    public static final String ENCRYPT_FIELD = ENCRYPT + StringConstants.DOT + "field";

    /**
     * 加密-API 加密
     */
    public static final String ENCRYPT_API = ENCRYPT + StringConstants.DOT + "api";

    /**
     * 安全配置
     */
    public static final String SECURITY = LEEPANDAR_STARTER + StringConstants.DOT + "security";

    /**
     * 安全-XSS 配置
     */
    public static final String SECURITY_XSS = SECURITY + StringConstants.DOT + "xss";

    /**
     * 安全-敏感词配置
     */
    public static final String SECURITY_SENSITIVE_WORDS = SECURITY + StringConstants.DOT + "sensitive-words";

    /**
     * 限流配置
     */
    public static final String RATE_LIMITER = LEEPANDAR_STARTER + StringConstants.DOT + "rate-limiter";

    /**
     * 幂等配置
     */
    public static final String IDEMPOTENT = LEEPANDAR_STARTER + StringConstants.DOT + "idempotent";

    /**
     * 链路追踪配置
     */
    public static final String TRACE = LEEPANDAR_STARTER + StringConstants.DOT + "trace";

    /**
     * 验证码配置
     */
    public static final String CAPTCHA = LEEPANDAR_STARTER + StringConstants.DOT + "captcha";

    /**
     * 图形验证码配置
     */
    public static final String CAPTCHA_GRAPHIC = CAPTCHA + StringConstants.DOT + "graphic";

    /**
     * 行为验证码配置
     */
    public static final String CAPTCHA_BEHAVIOR = CAPTCHA + StringConstants.DOT + "behavior";

    /**
     * 消息配置
     */
    public static final String MESSAGING = LEEPANDAR_STARTER + StringConstants.DOT + "messaging";

    /**
     * WebSocket 配置
     */
    public static final String MESSAGING_WEBSOCKET = MESSAGING + StringConstants.DOT + "websocket";

    /**
     * 日志配置
     */
    public static final String LOG = LEEPANDAR_STARTER + StringConstants.DOT + "log";

    /**
     * 存储配置
     */
    public static final String STORAGE = LEEPANDAR_STARTER + StringConstants.DOT + "storage";

    /**
     * License 配置
     */
    public static final String LICENSE = LEEPANDAR_STARTER + StringConstants.DOT + "license";

    /**
     * License 生成器配置
     */
    public static final String LICENSE_GENERATOR = LICENSE + StringConstants.DOT + "generator";

    /**
     * License 校验器配置
     */
    public static final String LICENSE_VERIFIER = LICENSE + StringConstants.DOT + "verifier";

    /**
     * CRUD 配置
     */
    public static final String CRUD = LEEPANDAR_STARTER + StringConstants.DOT + "crud";

    /**
     * 数据权限配置
     */
    public static final String DATA_PERMISSION = LEEPANDAR_STARTER + StringConstants.DOT + "data-permission";

    /**
     * 租户配置
     */
    public static final String TENANT = LEEPANDAR_STARTER + StringConstants.DOT + "tenant";

    private PropertiesConstants() {
    }
}
