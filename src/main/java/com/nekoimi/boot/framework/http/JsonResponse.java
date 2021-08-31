package com.nekoimi.boot.framework.http;

import com.nekoimi.boot.framework.contract.Jsonable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * nekoimi  2021/6/28 下午5:54
 */
@Slf4j
@Getter
@Setter
public class JsonResponse implements Jsonable {
    private final int code = 0;
    private final String message = "ok";
    private final long timestamp = System.currentTimeMillis();
    private Object data;

    public static <T> Mono<JsonResponse> ok() {
        return Mono.fromCallable(() -> {
            JsonResponse response = new JsonResponse();
            response.setData(Collections.emptyMap());
            return response;
        });
    }

    public static <T> Mono<JsonResponse> ok(T data) {
        return Mono.fromCallable(() -> {
            JsonResponse response = new JsonResponse();
            response.setData(data);
            return response;
        });
    }

    public static <T> Mono<JsonResponse> ok(Mono<T> data) {
        return data.map(t -> {
            JsonResponse response = new JsonResponse();
            response.setData(t);
            return response;
        });
    }
}
