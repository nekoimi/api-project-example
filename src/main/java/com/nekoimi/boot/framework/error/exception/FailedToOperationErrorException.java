package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 下午2:47
 */
public class FailedToOperationErrorException extends BaseRuntimeException {
    public FailedToOperationErrorException() {
        super(Errors.FAILED_TO_OPERATION_ERROR_EXCEPTION);
    }

    public FailedToOperationErrorException(String message) {
        super(Errors.FAILED_TO_OPERATION_ERROR_EXCEPTION, message);
    }
}
