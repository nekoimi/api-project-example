package com.nekoimi.boot.framework.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nekoimi.boot.common.utils.RequestUtils;
import com.nekoimi.boot.framework.constants.RequestConstants;
import com.nekoimi.boot.framework.contract.jwt.JWTService;
import com.nekoimi.boot.framework.contract.jwt.JWTSubject;
import com.nekoimi.boot.framework.contract.jwt.JWTSubjectService;
import com.nekoimi.boot.framework.error.exception.RequestAuthorizedException;
import com.nekoimi.boot.framework.error.exception.RequestBadCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Nekoimi  2020/5/31 21:19
 */
@Slf4j
@Component
public class LoginRequiredFilter implements Filter {
    private final JWTService jwtService;
    private final JWTSubjectService jwtSubjectService;
    private final RequestMappingHandlerMapping requestMappingHandler;

    public LoginRequiredFilter(JWTService jwtService, JWTSubjectService jwtSubjectService, RequestMappingHandlerMapping requestMappingHandler) {
        this.jwtService = jwtService;
        this.jwtSubjectService = jwtSubjectService;
        this.requestMappingHandler = requestMappingHandler;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.debug("-------------------------- LoginRequiredFilter BEGIN --------------------------");
        HandlerExecutionChain handler = null;
        try {
            handler = requestMappingHandler.getHandler(request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        if (handler != null) {
            doLoginRequired(request, response);
        }
        log.debug("-------------------------- LoginRequiredFilter END --------------------------");

        // next
        filterChain.doFilter(request, response);
    }

    /**
     *
     * @param request
     * @param response
     */
    private void doLoginRequired(HttpServletRequest request, HttpServletResponse response) {
        String token = RequestUtils.getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new RequestAuthorizedException("Please check the authorization parameters");
        }

        boolean needRefreshToken = false;
        String subId = null;
        String newToken = null;

        try {
            subId = jwtService.decode(token);
        } catch (TokenExpiredException ex) {
            // todo token过期 需要尝试无痛刷新
            newToken = jwtService.refresh(jwtSubjectService, token);
            subId = jwtService.decode(newToken);
            needRefreshToken = true;
        } catch (JWTVerificationException | IllegalArgumentException ex3) {
            throw new RequestBadCredentialsException();
        }

        if (StringUtils.isEmpty(subId)) {
            throw new RequestBadCredentialsException();
        }

        JWTSubject jwtSubject = jwtSubjectService.loadUserById(subId);
        String finalToken = StringUtils.isNotBlank(newToken) ? newToken : token;
        request.setAttribute(RequestConstants.REQUEST_USER, jwtSubject);
        if (needRefreshToken && StringUtils.isNotBlank(newToken)) {
            // todo 存在newToken 需要在response Header中带上新 token
            response.setHeader(RequestConstants.REQUEST_AUTHORIZATION, finalToken);
        }
    }

}
