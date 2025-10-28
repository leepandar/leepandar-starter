package com.leepandar.starter.storage.common.exception;

import com.leepandar.starter.core.exception.BaseException;

import java.io.Serial;

/**
 * 存储异常
 */
public class StorageException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StorageException() {
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
