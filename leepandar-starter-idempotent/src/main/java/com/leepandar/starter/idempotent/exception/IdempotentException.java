package com.leepandar.starter.idempotent.exception;

import com.leepandar.starter.core.exception.BaseException;

/**
 * 幂等异常
 */
public class IdempotentException extends BaseException {

    public IdempotentException(String message) {
        super(message);
    }

    public IdempotentException(String message, Throwable cause) {
        super(message, cause);
    }
}
