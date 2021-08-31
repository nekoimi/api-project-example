package com.nekoimi.boot.framework.http;

import com.nekoimi.boot.framework.contract.Jsonable;
import lombok.Builder;
import lombok.Data;

/**
 * nekoimi  2021/7/21 下午3:31
 */
@Data
@Builder(builderMethodName = "of")
public class ErrorResponse implements Jsonable {
    private int code;
    private String message;
    private String method;
    private String path;
    private final long timestamp = System.currentTimeMillis();
}
