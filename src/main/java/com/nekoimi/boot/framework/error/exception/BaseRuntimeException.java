package com.nekoimi.boot.framework.error.exception;

import com.nekoimi.boot.framework.contract.IError;
import com.nekoimi.boot.framework.error.enums.Errors;

/**
 * nekoimi  2021/7/19 上午11:13
 */
public abstract class BaseRuntimeException extends RuntimeException {
    private final static String JOIN_SYMBOL = " ";
    protected int code;
    protected IError error;
    public BaseRuntimeException() {
        this(Errors.DEFAULT_SERVER_ERROR);
    }
    public BaseRuntimeException(IError error) {
        this(error, "");
    }
    public BaseRuntimeException(IError error, String message) {
        super(error.getMessage() + JOIN_SYMBOL + message);
        this.code = error.getCode();
        this.error = error;
    }
    public int getCode() {
        return code;
    }
    public IError getError() {
        return error;
    }
}
