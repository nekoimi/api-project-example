package com.nekoimi.boot.framework.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/7/20 下午5:06
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.route")
public class RoutePatternsProperties {
}
