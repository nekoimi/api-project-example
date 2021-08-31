package com.nekoimi.boot.framework.error;

import com.nekoimi.boot.framework.http.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:30
 *
 * 异常处理
 */
public interface ErrorExceptionHandler<E extends Throwable> {
    /**
     * @return
     */
    Class<E> getType();

    /**
     * @param exchange
     * @param e
     * @return
     */
    Mono<ErrorResponse> handle(ServerWebExchange exchange, E e);
}
