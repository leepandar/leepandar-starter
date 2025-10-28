package com.leepandar.starter.ratelimiter.annotation;

import java.lang.annotation.*;

/**
 * 限流组注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiters {

    /**
     * 限流组
     */
    RateLimiter[] value();
}
