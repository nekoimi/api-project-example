package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 下午2:56
 */
public class IdGeneratorSystemClockException extends BaseRuntimeException {
    public IdGeneratorSystemClockException() {
        super(Errors.ID_GENERATOR_SYSTEM_CLOCK_EXCEPTION);
    }

    public IdGeneratorSystemClockException(String message) {
        super(Errors.ID_GENERATOR_SYSTEM_CLOCK_EXCEPTION, message);
    }
}
