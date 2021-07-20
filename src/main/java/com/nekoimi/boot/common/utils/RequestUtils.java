package com.nekoimi.boot.common.utils;

import com.nekoimi.boot.framework.constants.RequestConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * nekoimi  2021/7/7 上午10:34
 */
public class RequestUtils {
    private final static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static Boolean isGet(HttpServletRequest request) {
        return HttpMethod.GET.toString().equalsIgnoreCase(request.getMethod());
    }

    public static Boolean isPost(HttpServletRequest request) {
        return HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod());
    }

    public static Boolean isPut(HttpServletRequest request) {
        return HttpMethod.PUT.toString().equalsIgnoreCase(request.getMethod());
    }

    public static Boolean isDelete(HttpServletRequest request) {
        return HttpMethod.DELETE.toString().equalsIgnoreCase(request.getMethod());
    }

    public static Boolean isOptions(HttpServletRequest request) {
        return HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod());
    }

    public static Boolean isJsonRequest(HttpServletRequest request) {
        return StringUtils.containsIgnoreCase(request.getContentType(), "application/json");
    }

    public static boolean isMultipart(HttpServletRequest request) {
        return StringUtils.startsWithIgnoreCase(request.getContentType(), "multipart/");
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(RequestConstants.REQUEST_AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            return null;
        }

        if (StringUtils.startsWith(token, "Bearer ")) {
            token = StringUtils.substring(token, 7);
        }

        return token;
    }

    public static Integer getPage(HttpServletRequest request) {
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

    public static Integer getPageSize(HttpServletRequest request) {
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


    public static boolean has(HttpServletRequest request, String key) {
        return request.getParameter(key) != null;
    }

    public static String getString(HttpServletRequest request, String key) {
        return request.getParameter(key);
    }

    public static int getInteger(HttpServletRequest request, String key) {
        return Integer.parseInt(request.getParameter(key));
    }
}
