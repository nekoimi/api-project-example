package com.nekoimi.boot.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/7/19 下午3:03
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.id.generator")
public class IdGeneratorProperties {
    // 机器ID
    private long workerId;
    // 数据中心ID
    private long dataCenterId;
}
