package com.nekoimi.boot.common.utils;

import com.nekoimi.boot.framework.constants.RequestConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * nekoimi  2021/7/28 上午10:15
 */
public class RequestUtils {
    public static Boolean isGet(ServerHttpRequest request) {
        return HttpMethod.GET == request.getMethod();
    }

    public static Boolean isPost(ServerHttpRequest request) {
        return HttpMethod.POST == request.getMethod();
    }

    public static Boolean isPut(ServerHttpRequest request) {
        return HttpMethod.PUT == request.getMethod();
    }

    public static Boolean isDelete(ServerHttpRequest request) {
        return HttpMethod.DELETE == request.getMethod();
    }

    public static Boolean isOptions(ServerHttpRequest request) {
        return HttpMethod.OPTIONS == request.getMethod();
    }

    public static Boolean isJsonRequest(ServerHttpRequest request) {
        return request.getHeaders().getContentType() == MediaType.APPLICATION_JSON;
    }

    public static boolean isMultipart(ServerHttpRequest request) {
        MediaType contentType = request.getHeaders().getContentType();
        if (contentType == null) {
            return false;
        }
        return StringUtils.startsWithIgnoreCase(contentType.toString(), "multipart/");
    }

    public static String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(RequestConstants.REQUEST_AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            return null;
        }

        if (StringUtils.startsWith(token, "Bearer ")) {
            token = StringUtils.substring(token, 7);
        }

        return token;
    }

    public static Integer getPage(ServerHttpRequest request) {
        int page = 1;
        String[] pageKeys = new String[] {"page", "pageNum", "page_num", "page_index", "pageIndex"};
        for (String pageKey: pageKeys) {
            if (has(request, pageKey)) {
                page = getInteger(request, pageKey);
                page = page <= 0 ? 1 : page;
            }
        }
        return page;
    }

    public static Integer getPageSize(ServerHttpRequest request) {
        int pageSize = 10;
        String[] pageKeys = new String[] {"pageSize", "page_size"};
        for (String pageKey: pageKeys) {
            if (has(request, pageKey)) {
                pageSize = getInteger(request, pageKey);
                return pageSize <= 0 ? 10 : pageSize;
            }
        }
        return pageSize;
    }


    public static boolean has(ServerHttpRequest request, String key) {
        return request.getQueryParams().getFirst(key) != null;
    }

    public static String getString(ServerHttpRequest request, String key) {
        return request.getQueryParams().getFirst(key);
    }

    public static int getInteger(ServerHttpRequest request, String key) {
        if (!has(request, key)) {
            return -1;
        }
        return Integer.parseInt(getString(request, key));
    }
}
