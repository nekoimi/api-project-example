package com.nekoimi.boot.framework.error;

import com.nekoimi.boot.framework.http.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午4:42
 */
@Slf4j
@Component
public class DefaultErrorResponseProducer implements ErrorResponseProducer {

    @Override
    public Mono<Void> produce(ServerWebExchange exchange, ErrorResponse errorResponse) {
        return Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            preHandle(response);
            String json = errorResponse.toJson();
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes());
            return response.writeWith(Mono.just(buffer)).doOnError(e -> DataBufferUtils.release(buffer));
        });
    }

    private void preHandle(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }
}
