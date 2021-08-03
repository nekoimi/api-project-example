package com.nekoimi.boot.framework.http;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * nekoimi  2021/6/28 下午5:54
 */
@Slf4j
@Data
@Builder
public class JsonResponse {
    private final int code;
    private final String message;
    private final String method;
    private final String path;
    private final long timestamp;
    private final Object data;
}
