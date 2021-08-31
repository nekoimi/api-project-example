package com.nekoimi.boot.framework.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/7/20 下午5:06
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.route")
public class RoutePatternsProperties {
    private PathPattern authorization = new PathPattern();

    @Getter
    @Setter
    public static final class PathPattern {
        private List<String> exclude = new ArrayList<>();
    }
}
