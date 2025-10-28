package com.leepandar.starter.encrypt.api.annotation;

import java.lang.annotation.*;

/**
 * API 加密注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEncrypt {

    /**
     * 是否加密响应
     */
    boolean response() default true;
}
