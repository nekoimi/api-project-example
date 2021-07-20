package com.nekoimi.boot.framework.configuration;

import com.nekoimi.boot.framework.contract.jwt.JWTSubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nekoimi  2021/7/20 上午9:51
 */
@Slf4j
@Configuration
public class AppConfiguration {
    public AppConfiguration() {
        log.debug("[Auto Configuration] AppConfiguration!");
    }

    @Bean
    @ConditionalOnMissingBean(value = JWTSubjectService.class, search = SearchStrategy.CURRENT)
    public JWTSubjectService sampleJWTSubjectService() {
        return subId -> null;
    }
}
