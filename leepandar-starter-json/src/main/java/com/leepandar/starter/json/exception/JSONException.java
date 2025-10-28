package com.leepandar.starter.json.exception;

import com.leepandar.starter.core.exception.BaseException;

import java.io.Serial;

/**
 * JSON 异常
 */
public class JSONException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public JSONException() {
    }

    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable cause) {
        super(cause);
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }
}
