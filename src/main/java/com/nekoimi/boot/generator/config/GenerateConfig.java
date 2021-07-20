package com.nekoimi.boot.generator.config;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * nekoimi  2021/6/13 下午5:45
 *
 * 代码生成器基本配置
 */
@Getter
@Setter
public class GenerateConfig {
    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 路由API中resource的名称
     */
    private String routeName;

    /**
     * 代码生成的类的父包名称
     */
    private String parentPackage;

    /**
     * api版本
     */
    private String apiVersion;

    /**
     * 代码生成包含的表，可为空，为空默认生成所有
     */
    private String[] includeTables;

    /**
     * 生成代码里，注释的作者
     */
    private String author;

    /**
     * 数据库类型
     */
    private DbType dbType;

    /**
     * jdbc驱动
     */
    private String jdbcDriver;

    /**
     * 数据库连接地址
     */
    private String jdbcUrl;

    /**
     * 数据库账号
     */
    private String jdbcUserName;

    /**
     * 数据库密码
     */
    private String jdbcPassword;

    /**
     * 代码生成目录
     */
    private String outputDir;
}
