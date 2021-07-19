package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 下午2:42
 */
public class FailedToUpdateErrorException extends BaseRuntimeException {
    public FailedToUpdateErrorException() {
        super(Errors.FAILED_TO_UPDATE_ERROR_EXCEPTION);
    }

    public FailedToUpdateErrorException(String message) {
        super(Errors.FAILED_TO_UPDATE_ERROR_EXCEPTION, message);
    }
}
