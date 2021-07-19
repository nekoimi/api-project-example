package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 上午11:13
 */
public class RequestValidationException extends BaseRuntimeException {
    public RequestValidationException() {
        super(Errors.HTTP_REQUEST_VALIDATE_EXCEPTION);
    }

    public RequestValidationException(String message) {
        super(Errors.HTTP_REQUEST_VALIDATE_EXCEPTION, message);
    }
}
