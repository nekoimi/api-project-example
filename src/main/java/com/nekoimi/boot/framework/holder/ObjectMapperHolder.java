package com.nekoimi.boot.framework.holder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * nekoimi  2021/7/20 上午9:39
 */
public class ObjectMapperHolder {
    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        ObjectMapperHolder.objectMapper = objectMapper;
    }
}
