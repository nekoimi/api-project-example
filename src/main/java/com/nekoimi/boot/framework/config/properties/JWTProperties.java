package com.nekoimi.boot.framework.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/7/19 下午3:37
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JWTProperties {
    private String secret;
    private Integer ttl;
    private Integer refreshTtl;
    private Integer refreshConcurrentTtl;
}
