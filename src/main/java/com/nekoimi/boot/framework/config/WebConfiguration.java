package com.nekoimi.boot.framework.config;

import com.nekoimi.boot.common.annotaction.resolver.OperatorArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

/**
 * nekoimi  2021/7/21 上午11:25
 */
@Slf4j
@Configuration
public class WebConfiguration implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");

        log.debug("Cors registry!");
    }

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new OperatorArgumentResolver());

        log.debug("Argument resolver registry!");
    }
}
