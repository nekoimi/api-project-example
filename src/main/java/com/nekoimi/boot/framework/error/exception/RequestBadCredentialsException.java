package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/20 下午4:44
 */
public class RequestBadCredentialsException extends BaseRuntimeException {
    public RequestBadCredentialsException() {
        super(Errors.REQUEST_BAD_CREDENTIALS_EXCEPTION);
    }

    public RequestBadCredentialsException(String message) {
        super(Errors.REQUEST_BAD_CREDENTIALS_EXCEPTION, message);
    }
}
