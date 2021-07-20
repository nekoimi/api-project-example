package com.nekoimi.boot.common.annotaction.resolver;

import com.nekoimi.boot.common.annotaction.Operator;
import com.nekoimi.boot.framework.constants.RequestConstants;
import com.nekoimi.boot.framework.error.exception.RequestAuthorizedException;
import com.nekoimi.boot.framework.error.exception.RequestForbiddenException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Parameter;

/**
 * nekoimi  2021/7/2 下午3:11
 */
public class OperatorArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Operator.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object o = nativeWebRequest.getAttribute(RequestConstants.REQUEST_USER, RequestAttributes.SCOPE_REQUEST);
        if (o == null) {
            throw new RequestAuthorizedException();
        }
        Parameter p = methodParameter.getParameter();
        Class<?> typeClazz = p.getType();
        if (!typeClazz.isInstance(o)) {
            throw new RequestForbiddenException();
        }
        return o;
    }
}
