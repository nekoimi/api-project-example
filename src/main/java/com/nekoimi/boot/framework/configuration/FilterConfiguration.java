package com.nekoimi.boot.framework.configuration;

import com.nekoimi.boot.framework.contract.jwt.JWTService;
import com.nekoimi.boot.framework.contract.jwt.JWTSubjectService;
import com.nekoimi.boot.framework.filter.BeforeRequestFilter;
import com.nekoimi.boot.framework.filter.LoginRequiredFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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
     *
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

    /**
     * 对请求进行token验证
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> registerLoginRequiredFilter(
            JWTService jwtService,
            JWTSubjectService jwtSubjectService,
            RequestMappingHandlerMapping requestMappingHandler
    ) {
        log.debug("LoginRequiredFilter registry!");

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginRequiredFilter(jwtService, jwtSubjectService, requestMappingHandler));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1024);
        return registrationBean;
    }

}
