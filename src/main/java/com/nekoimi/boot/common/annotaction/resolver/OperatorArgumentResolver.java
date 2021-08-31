package com.nekoimi.boot.common.annotaction.resolver;

import com.nekoimi.boot.common.annotaction.Operator;
import com.nekoimi.boot.framework.constants.RequestConstants;
import com.nekoimi.boot.framework.error.exception.RequestAuthorizedException;
import com.nekoimi.boot.framework.error.exception.RequestForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Parameter;

/**
 * nekoimi  2021/7/2 下午3:11
 */
@Slf4j
public class OperatorArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Operator.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter methodParameter, BindingContext ctx, ServerWebExchange serverWebExchange) {
        Object o = ctx.getModel().getAttribute(RequestConstants.REQUEST_USER);
        if (o == null) {
            throw new RequestAuthorizedException();
        }
        Parameter p = methodParameter.getParameter();
        Class<?> typeClazz = p.getType();
        log.debug("Resolve operator argument {}", typeClazz);
        if (!typeClazz.isInstance(o)) {
            throw new RequestForbiddenException();
        }
        return Mono.just(o);
    }

}
