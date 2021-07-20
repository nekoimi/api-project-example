package com.nekoimi.boot.framework.http;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nekoimi.boot.common.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    protected JsonResponse ok() {
        return JsonResponse.ok().withMethod(request.getMethod()).withPath(request.getRequestURI()).build();
    }

    protected JsonResponse ok(Object data) {
        return JsonResponse.ok(data).withMethod(request.getMethod()).withPath(request.getRequestURI()).build();
    }

    protected JsonResponse ok(String name, Object data) {
        return JsonResponse.ok(Map.of(name, data)).withMethod(request.getMethod()).withPath(request.getRequestURI()).build();
    }

    protected <T> JsonResponse ok(IPage<T> page) {
        return JsonResponse.ok(page).withMethod(request.getMethod()).withPath(request.getRequestURI()).build();
    }
}
