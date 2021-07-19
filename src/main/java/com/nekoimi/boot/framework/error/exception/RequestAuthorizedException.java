package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 下午3:16
 */
public class RequestAuthorizedException extends BaseRuntimeException {
    public RequestAuthorizedException() {
        super(Errors.HTTP_STATUS_UNAUTHORIZED);
    }
}
