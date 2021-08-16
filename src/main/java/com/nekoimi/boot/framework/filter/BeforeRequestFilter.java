package com.nekoimi.boot.framework.filter;

import com.nekoimi.boot.common.utils.RequestUtils;
import com.nekoimi.boot.framework.http.HttpRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * nekoimi  2021/7/20 下午2:47
 */
@Slf4j
@Component
public class BeforeRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getRequestURI().contains("favicon.ico")) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.getOutputStream().print(HttpStatus.NO_CONTENT.getReasonPhrase());
            return;
        }

        log.debug("-------------------------- BeforeRequestFilter BEGIN --------------------------");
        if (RequestUtils.isPost(request) && RequestUtils.isMultipart(request)) {
            filterChain.doFilter(request, response);
        } else {
            // next
            filterChain.doFilter(new HttpRequestWrapper(request), response);
        }
        log.debug("-------------------------- BeforeRequestFilter END --------------------------");
    }
}
