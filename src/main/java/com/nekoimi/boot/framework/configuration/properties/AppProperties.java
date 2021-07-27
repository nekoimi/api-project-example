package com.nekoimi.boot.framework.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/7/20 上午10:49
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.main")
public class AppProperties {
    private String appKey;
}
