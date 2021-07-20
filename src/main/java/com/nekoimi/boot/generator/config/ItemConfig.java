package com.nekoimi.boot.generator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * nekoimi  2021/7/20 下午3:50
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemConfig {
    private String moduleName;
    private String routeName;
    private String tableName;
}
