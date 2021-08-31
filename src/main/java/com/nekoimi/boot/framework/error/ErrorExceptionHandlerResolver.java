package com.nekoimi.boot.framework.error;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:43
 */
public interface ErrorExceptionHandlerResolver {
    /**
     * 获取指定异常的处理器
     * @param e
     * @return
     */
    Mono<ErrorExceptionHandler> resolve(Throwable e);
}
