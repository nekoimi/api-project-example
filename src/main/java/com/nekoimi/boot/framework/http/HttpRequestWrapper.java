package com.nekoimi.boot.framework.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * nekoimi  2021/7/20 下午2:23
 */
@Slf4j
public class HttpRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public HttpRequestWrapper(HttpServletRequest request) {
        super(request);
        byte[] bytes = new byte[0];
        try {
            bytes = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            log.error("Read request body error! " + e.getMessage());
        }
        body = bytes;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return ObjectUtils.isEmpty(body) ? null : new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private static Boolean isNotIpAddress(String ipAddress) {
        return (StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress));
    }

    @Override
    public String getRemoteAddr() {
        String ipAddress = getHeader("X-Forwarded-For");
        if (isNotIpAddress(ipAddress)) {
            ipAddress = getHeader("Proxy-Client-IP");
        }
        if (isNotIpAddress(ipAddress)) {
            ipAddress = getHeader("WL-Proxy-Client-IP");
        }
        if (isNotIpAddress(ipAddress)) {
            ipAddress = super.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();

                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException ex) {
                    log.error(ex.getMessage());

                    ex.printStackTrace();
                }
            }
        }
        if (!isNotIpAddress(ipAddress)) {
            if (ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        }
        return ipAddress;
    }

    public Map<String, Object> getQueryMap() {
        Map<String, Object> map = new HashMap<>();
        String query = getQueryString();
        if (StringUtils.isBlank(query)) {
            return map;
        }
        if (query.startsWith("?")) {
            query = query.substring(1);
        }
        String[] kvList = query.split("[&]");
        for (String kv : kvList) {
            String[] kAndV = kv.split("[=]");
            if (kAndV.length >= 2) {
                map.put(kAndV[0], URLDecoder.decode(kAndV[1], StandardCharsets.UTF_8));
            } else {
                map.put(kAndV[0], "");
            }
        }
        return map;
    }

}
