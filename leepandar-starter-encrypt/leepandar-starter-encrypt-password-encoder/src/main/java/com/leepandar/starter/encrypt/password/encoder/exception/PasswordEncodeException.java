package com.leepandar.starter.encrypt.password.encoder.exception;

import com.leepandar.starter.core.exception.BaseException;

import java.io.Serial;

/**
 * 密码编码异常
 */
public class PasswordEncodeException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PasswordEncodeException() {
    }

    public PasswordEncodeException(String message) {
        super(message);
    }

    public PasswordEncodeException(Throwable cause) {
        super(cause);
    }

    public PasswordEncodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
