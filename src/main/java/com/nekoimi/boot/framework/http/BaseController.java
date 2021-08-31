package com.nekoimi.boot.framework.http;

import com.nekoimi.boot.common.utils.RequestUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * nekoimi  2021/7/28 上午10:14
 */
public abstract class BaseController {
    protected int page(ServerWebExchange exchange) {
        return RequestUtils.getPage(exchange.getRequest());
    }

    protected int pageSize(ServerWebExchange exchange) {
        return RequestUtils.getPageSize(exchange.getRequest());
    }
}
