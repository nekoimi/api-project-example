package com.nekoimi.boot.framework.configuration;

import com.nekoimi.boot.framework.filter.BeforeRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.Filter;

/**
 * nekoimi  2021/7/20 下午2:59
 */
@Slf4j
@Configuration
public class FilterConfiguration {

    public FilterConfiguration() {
        log.debug("[Auto Configuration] FilterConfiguration!");
    }

    /**
     * 对请求进行预处理
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> registerBeforeRequestFilter() {
        log.debug("BeforeRequestFilter registry!");

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new BeforeRequestFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}
