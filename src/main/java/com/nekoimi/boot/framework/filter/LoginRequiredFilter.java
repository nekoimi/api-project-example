package com.nekoimi.boot.framework.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nekoimi.boot.common.utils.RequestUtils;
import com.nekoimi.boot.framework.constants.RequestConstants;
import com.nekoimi.boot.framework.contract.jwt.JWTService;
import com.nekoimi.boot.framework.contract.jwt.JWTSubjectService;
import com.nekoimi.boot.framework.error.exception.RequestAuthorizedException;
import com.nekoimi.boot.framework.error.exception.RequestBadCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Nekoimi  2020/5/31 21:19
 */
@Slf4j
@Component
public class LoginRequiredFilter implements WebFilter {
    private final JWTService jwtService;
    private final JWTSubjectService jwtSubjectService;
    private final RequestMappingHandlerMapping handlerMapping;

    public LoginRequiredFilter(JWTService jwtService,
                               JWTSubjectService jwtSubjectService,
                               RequestMappingHandlerMapping handlerMapping) {
        this.jwtService = jwtService;
        this.jwtSubjectService = jwtSubjectService;
        this.handlerMapping = handlerMapping;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
        log.debug("-------------------------- LoginRequiredFilter BEGIN --------------------------");
        return handlerMapping.getHandler(exchange).flatMap(o -> doLoginRequired(exchange, o)).switchIfEmpty(filterChain.filter(exchange)).then();
    }

    /**
     * @param exchange
     * @param o
     */
    private Mono<Object> doLoginRequired(ServerWebExchange exchange, Object o) {
        if (o == null) return Mono.empty();
        String token = RequestUtils.getToken(exchange.getRequest());
        log.debug("doLoginRequired - token: {}", token);
        if (StringUtils.isBlank(token)) {
            return Mono.error(new RequestAuthorizedException("Please check the authorization parameters of the request headers"));
        }
        return jwtService.decode(token).onErrorResume(e -> {
            if (e instanceof TokenExpiredException) {
                // 刷新Token
                return Mono.just(token).flatMap(oldToken -> jwtService.refresh(jwtSubjectService, oldToken).flatMap(newToken -> {
                    exchange.getResponse().getHeaders().set(RequestConstants.REQUEST_AUTHORIZATION, newToken);
                    return Mono.just(newToken);
                }).flatMap(jwtService::decode));
            }
            log.error(e.getMessage());
            return Mono.error(new RequestBadCredentialsException(e.getMessage()));
        }).flatMap(subId -> {
            if (StringUtils.isBlank(subId)) return Mono.error(new RequestBadCredentialsException());
            return jwtSubjectService.loadUserById(subId).flatMap(jwtSubject -> {
                exchange.getAttributes().put(RequestConstants.REQUEST_USER, jwtSubject);
                return Mono.just(jwtSubject);
            });
        });
    }
}
