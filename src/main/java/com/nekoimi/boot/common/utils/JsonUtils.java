package com.nekoimi.boot.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nekoimi.boot.framework.holder.ObjectMapperHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * nekoimi  2021/6/29 下午1:51
 */
@Slf4j
public class JsonUtils {

    public String toJson(Object src) {
        try {
            return ObjectMapperHolder.getObjectMapper().writeValueAsString(src);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public <T> T toBean(String json, Class<T> resultType) {
        try {
            return ObjectMapperHolder.getObjectMapper().readValue(json, resultType);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
