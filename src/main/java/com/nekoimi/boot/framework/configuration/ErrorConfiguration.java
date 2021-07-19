package com.nekoimi.boot.framework.configuration;

import com.nekoimi.boot.framework.error.SampleErrorAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nekoimi  2021/7/17 上午12:59
 */
@Slf4j
@Configuration
public class ErrorConfiguration {

    public ErrorConfiguration() {
        log.debug("[Auto Configuration] ErrorConfiguration!");
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        log.debug("SampleErrorAttributes registry!");

        return new SampleErrorAttributes();
    }

}
