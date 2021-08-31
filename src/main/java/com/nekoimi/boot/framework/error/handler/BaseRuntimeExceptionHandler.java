package com.nekoimi.boot.framework.error.handler;

import com.nekoimi.boot.framework.error.ErrorExceptionHandler;
import com.nekoimi.boot.framework.error.exception.BaseRuntimeException;
import com.nekoimi.boot.framework.http.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/28 上午9:06
 *
 * 自定义的异常处理
 */
@Slf4j
@Component
public class BaseRuntimeExceptionHandler implements ErrorExceptionHandler<BaseRuntimeException> {
    @Override
    public Class<BaseRuntimeException> getType() {
        return BaseRuntimeException.class;
    }

    @Override
    public Mono<ErrorResponse> handle(ServerWebExchange exchange, BaseRuntimeException e) {
        ServerHttpRequest request = exchange.getRequest();
        return Mono.fromCallable(() -> ErrorResponse.of()
                .code(e.getCode())
                .message(e.getMessage())
                .method(request.getMethodValue())
                .path(request.getPath().value())
                .build());
    }
}
