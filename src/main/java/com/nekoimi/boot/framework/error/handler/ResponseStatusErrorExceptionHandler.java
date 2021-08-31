package com.nekoimi.boot.framework.error.handler;

import com.nekoimi.boot.framework.contract.IError;
import com.nekoimi.boot.framework.error.ErrorExceptionHandler;
import com.nekoimi.boot.framework.error.enums.Errors;
import com.nekoimi.boot.framework.http.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:34
 *
 * HTTP的异常处理
 */
@Slf4j
@Component
public class ResponseStatusErrorExceptionHandler implements ErrorExceptionHandler<ResponseStatusException> {
    @Override
    public Class<ResponseStatusException> getType() {
        return ResponseStatusException.class;
    }

    @Override
    public Mono<ErrorResponse> handle(ServerWebExchange exchange, ResponseStatusException e) {
        HttpStatus status = e.getStatus();
        ServerHttpRequest request = exchange.getRequest();
        return Mono.fromCallable(() -> {
            IError error = Errors.DEFAULT_SERVER_ERROR;
            if (status.is4xxClientError()) {
                error = buildHttpStatusClientError(status);
            } else if (status.is5xxServerError()) {
                error = buildHttpStatusServerError(status);
            }
            return ErrorResponse.of()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .method(request.getMethodValue())
                    .path(request.getPath().value())
                    .build();
        });
    }

    private IError buildHttpStatusClientError(HttpStatus status) {
        return getClientError(status);
    }

    private IError buildHttpStatusServerError(HttpStatus status) {
        log.error("Http status server error! {}", status);
        return Errors.DEFAULT_SERVER_ERROR;
    }

    public static IError getClientError(HttpStatus status) {
        switch (status) {
            case BAD_REQUEST:
                return Errors.HTTP_STATUS_BAD_REQUEST;
            case UNAUTHORIZED:
                return Errors.HTTP_STATUS_UNAUTHORIZED;
            case FORBIDDEN:
                return Errors.HTTP_STATUS_FORBIDDEN;
            case NOT_FOUND:
                return Errors.HTTP_STATUS_NOT_FOUND;
            case METHOD_NOT_ALLOWED:
                return Errors.HTTP_STATUS_METHOD_NOT_ALLOWED;
        }
        return Errors.DEFAULT_CLIENT_ERROR;
    }
}
