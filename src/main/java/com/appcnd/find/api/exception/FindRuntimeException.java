package com.appcnd.find.api.exception;

/**
 * @author nihao 2018/11/23
 */
public class FindRuntimeException extends RuntimeException {
    public FindRuntimeException() {
    }

    public FindRuntimeException(String message) {
        super(message);
    }

    public FindRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FindRuntimeException(Throwable cause) {
        super(cause);
    }

    public FindRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
