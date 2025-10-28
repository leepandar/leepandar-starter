package com.leepandar.starter.ratelimiter.exception;

import  com.leepandar.starter.core.exception.BaseException;

/**
 * 限流异常
 */
public class RateLimiterException extends BaseException {

    public RateLimiterException(String message) {
        super(message);
    }

    public RateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }
}
