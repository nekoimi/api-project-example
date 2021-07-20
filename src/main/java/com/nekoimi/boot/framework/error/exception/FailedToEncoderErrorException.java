package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/20 上午11:01
 */
public class FailedToEncoderErrorException extends BaseRuntimeException {
    public FailedToEncoderErrorException() {
        super(Errors.FAILED_TO_ENCODER_ERROR_EXCEPTION);
    }

    public FailedToEncoderErrorException(String message) {
        super(Errors.FAILED_TO_ENCODER_ERROR_EXCEPTION, message);
    }
}
