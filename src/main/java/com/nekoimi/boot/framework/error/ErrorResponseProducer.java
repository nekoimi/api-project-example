package com.nekoimi.boot.framework.error;

import com.nekoimi.boot.framework.http.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午4:41
 *
 * 异常消息返回
 */
public interface ErrorResponseProducer {
    /**
     *
     * @param exchange
     * @param errorResponse
     * @return
     */
    Mono<Void> produce(ServerWebExchange exchange, ErrorResponse errorResponse);
}
