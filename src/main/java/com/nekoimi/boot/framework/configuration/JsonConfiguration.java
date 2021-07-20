package com.nekoimi.boot.framework.configuration;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.nekoimi.boot.common.utils.ObjectMapperHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * nekoimi  2021/7/19 上午10:31
 */
@Slf4j
@Configuration
public class JsonConfiguration {
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT, Locale.CHINA);
    private static final TimeZone DEFAULT_DATE_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    public JsonConfiguration() {
        log.debug("[Auto Configuration] JsonConfiguration!");
    }

    /**
     * Jackson全局配置
     *
     * @param properties
     * @return
     */
    @Bean
    @Primary
    public JacksonProperties jacksonProperties(JacksonProperties properties) {
        properties.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        properties.getSerialization().put(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        properties.getSerialization().put(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        properties.getSerialization().put(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        properties.getDeserialization().put(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        properties.getDeserialization().put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        properties.setDateFormat(DEFAULT_DATE_TIME_FORMAT);
        properties.setTimeZone(DEFAULT_DATE_TIME_ZONE);
        return properties;
    }

    /**
     * ObjectMapper Builder
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer builderCustomizer() {
        return builder -> {
            builder.serializationInclusion(JsonInclude.Include.ALWAYS);
            // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
            builder.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            // 日期格式
            builder.dateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale.CHINA));
            builder.timeZone(DEFAULT_DATE_TIME_ZONE);
            // 关闭
            builder.featuresToDisable(
                    DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    SerializationFeature.FAIL_ON_EMPTY_BEANS
            );
            // 开启
            builder.featuresToEnable(
                    SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
                    SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS
            );
            builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
        };
    }

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DEFAULT_DATE_TIME_FORMATTER);
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DEFAULT_DATE_TIME_FORMATTER);
    }

    /**
     * 转换器全局配置
     *
     * @param converters
     * @return
     */
    @Bean
    public HttpMessageConverters httpMessageConverters(List<HttpMessageConverter<?>> converters, ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        JacksonTypeHandler.setObjectMapper(objectMapper);
        ObjectMapperHolder.setObjectMapper(objectMapper);
        return new HttpMessageConverters(jackson2HttpMessageConverter);
    }

}
