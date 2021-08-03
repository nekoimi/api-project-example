package com.nekoimi.boot.framework.http;

import com.nekoimi.boot.common.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * nekoimi  2021/7/20 下午3:07
 */
public abstract class BaseController {
    @Autowired
    protected HttpServletRequest request;

    protected int page() {
        return RequestUtils.getPage(request);
    }

    protected int pageSize() {
        return RequestUtils.getPageSize(request);
    }
}
