package com.nekoimi.boot.framework.error;

import com.nekoimi.boot.framework.http.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/5/25 下午4:05
 *
 * 全局异常处理
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
public class ServerErrorHandler implements ErrorWebExceptionHandler {
    private final ErrorResponseProducer responseProducer;
    private final ErrorExceptionHandlerResolver handlerResolver;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        return handlerResolver.resolve(e).flatMap(handler -> (Mono<ErrorResponse>) handler.handle(exchange, e)).flatMap(errorResponse -> responseProducer.produce(exchange, errorResponse));
    }
}
