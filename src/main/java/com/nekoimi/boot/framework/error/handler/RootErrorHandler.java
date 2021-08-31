package com.nekoimi.boot.framework.error.handler;

import com.nekoimi.boot.framework.contract.IError;
import com.nekoimi.boot.framework.error.ErrorExceptionHandler;
import com.nekoimi.boot.framework.error.enums.Errors;
import com.nekoimi.boot.framework.http.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:46
 */
@Slf4j
@Component
public class RootErrorHandler implements ErrorExceptionHandler<Error> {
    private static final IError error = Errors.DEFAULT_SERVER_ERROR;

    @Override
    public Class<Error> getType() {
        return Error.class;
    }

    @Override
    public Mono<ErrorResponse> handle(ServerWebExchange exchange, Error e) {
        ServerHttpRequest request = exchange.getRequest();
        log.error("Unprocessed Error: {}", e.getClass());
        log.error("Unprocessed ErrorMessage: {}", e.getMessage());
        return Mono.fromCallable(() -> ErrorResponse.of()
                .code(error.getCode())
                .message(error.getMessage())
                .method(request.getMethodValue())
                .path(request.getPath().value())
                .build())
                .doOnSuccess(r -> log.error("error: {}, message: {}", e.getClass(), e.getMessage()));
    }
}
