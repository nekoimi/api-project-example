package com.nekoimi.boot.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * nekoimi  2021/6/29 下午1:51
 */
@Slf4j
public class JsonUtils {
    private ObjectMapper objectMapper;
    private Object src;

    private JsonUtils(Object src) {
        this.src = src;
        this.objectMapper = new ObjectMapper();
    }

    public static JsonUtils of(Object bean) {
        return new JsonUtils(bean);
    }

    public static JsonUtils of(String json) {
        return new JsonUtils(json);
    }

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public <T> T toBean(Class<T> resultType) {
        try {
            return objectMapper.readValue(src.toString(), resultType);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
