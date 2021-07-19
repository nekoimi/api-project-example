package com.nekoimi.boot.framework.configuration;

import com.nekoimi.boot.common.annotaction.resolver.OperatorArgumentResolver;
import com.nekoimi.boot.framework.constants.RequestConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * nekoimi  2021/7/19 上午11:56
 */
@Slf4j
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    public WebConfiguration() {
        log.debug("[Auto Configuration] WebConfiguration!");
    }

    /**
     * 参数注入
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new OperatorArgumentResolver());
        log.debug("ArgumentResolver registry!");
    }

    /**
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(0L)
                .allowedOrigins("*")
                .allowedHeaders("*")
                .exposedHeaders(RequestConstants.REQUEST_AUTHORIZATION)
                .allowedMethods("GET", "OPTIONS", "POST", "PUT", "DELETE")
                .allowCredentials(true);        //是否可以将对请求的响应暴露给页面

        log.debug("Cors registry!");
    }

}
