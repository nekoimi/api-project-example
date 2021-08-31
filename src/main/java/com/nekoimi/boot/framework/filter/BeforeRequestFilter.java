package com.nekoimi.boot.framework.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/20 下午2:47
 */
@Slf4j
@Component
public class BeforeRequestFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (request.getPath().value().contains("favicon")) {
            response.setStatusCode(HttpStatus.NO_CONTENT);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(HttpStatus.NO_CONTENT.getReasonPhrase().getBytes())));
        }
        log.debug("-------------------------- BeforeRequestFilter BEGIN --------------------------");

        return filterChain.filter(exchange);
    }
}
