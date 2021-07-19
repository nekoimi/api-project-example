package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 下午3:58
 */
public class RedisErrorException extends BaseRuntimeException {
    public RedisErrorException() {
        super(Errors.REDIS_ERROR_EXCEPTION);
    }

    public RedisErrorException(String message) {
        super(Errors.REDIS_ERROR_EXCEPTION, message);
    }
}
