package com.nekoimi.boot.framework.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;

/**
 * nekoimi  2021/7/3 上午10:07
 *
 * 统一返回值
 */
@Slf4j
@Component
@RestControllerAdvice(basePackages = {"com.nekoimi.boot.modules"})
public class ResponseBodyWriteHandler implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            body = new HashMap<>();
        }
        log.debug(body.getClass().toString());
        log.debug(body.toString());
        log.debug(returnType.toString());
        log.debug(selectedContentType.toString());
        log.debug(selectedConverterType.toString());
        log.debug(request.getClass().toString());
        log.debug(response.getClass().toString());

        return JsonResponse.builder().code(0).message("ok")
                .method(request.getMethodValue())
                .path(request.getURI().getPath())
                .timestamp(System.currentTimeMillis())
                .data(body)
                .build();
    }
}
