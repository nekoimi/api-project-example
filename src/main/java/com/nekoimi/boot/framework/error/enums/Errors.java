package com.nekoimi.boot.framework.error.enums;

import com.nekoimi.boot.framework.contract.IError;

/**
 * nekoimi  2021/7/19 上午10:27
 */
public enum Errors implements IError {
    //
    // code 从 10000 开始往上加，越是底层错误 code 越大
    //

    // 和 Http 相近的异常， code 从 10000 开始
    REQUEST_VALIDATE_EXCEPTION(10004, "Invalid request"),

    TOKEN_CANNOT_BE_REFRESH_EXCEPTION(10100, "Token cannot be refresh"),
    TOKEN_HAS_BEEN_BLACKLISTED_EXCEPTION(10101, "The token has been blacklisted"),

    HTTP_STATUS_BAD_REQUEST(10400, "Bad request"),
    HTTP_STATUS_UNAUTHORIZED(10401, "Unauthorized"),
    HTTP_STATUS_FORBIDDEN(10403, "Forbidden"),
    HTTP_STATUS_NOT_FOUND(10404, "Not found"),
    HTTP_STATUS_METHOD_NOT_ALLOWED(10405, "Method not allowed"),
    // 业务相关异常，code 从 20000 开始往上加
    FAILED_TO_OPERATION_ERROR_EXCEPTION(20001, "Operation failed"),
    FAILED_TO_NOT_FOUND_ERROR_EXCEPTION(20002, "Resource not found"),
    FAILED_TO_CREATE_ERROR_EXCEPTION(20003, "Failed to create resource"),
    FAILED_TO_UPDATE_ERROR_EXCEPTION(20004, "Failed to update resource"),

    // 底层中间件相关异常，code 从 50000 开始
    ID_GENERATOR_SYSTEM_CLOCK_EXCEPTION(50000, "System clock error"),
    REDIS_ERROR_EXCEPTION(50001, "Redis error"),

    // 客户端请求异常
    DEFAULT_CLIENT_ERROR(99400, "无效的请求！"),
    // 未捕获的异常，系统发生致命错误，提示系统维护更新!
    DEFAULT_SERVER_ERROR(99500, "系统更新中！请稍候再试～");

    private final int code;
    private final String message;

    Errors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
