package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 下午2:38
 */
public class FailedToCreateErrorException extends BaseRuntimeException {
    public FailedToCreateErrorException() {
        super(Errors.FAILED_TO_CREATE_ERROR_EXCEPTION);
    }

    public FailedToCreateErrorException(String message) {
        super(Errors.FAILED_TO_CREATE_ERROR_EXCEPTION, message);
    }
}
