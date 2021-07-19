package com.nekoimi.boot.framework.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.nekoimi.boot.common.properties.IdGeneratorProperties;
import com.nekoimi.boot.framework.gen.SnowflakeIdGenerator;
import com.nekoimi.boot.framework.mybatis.handler.MybatisPlusMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * nekoimi  2020/7/6 下午3:51
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfiguration {

    public MyBatisPlusConfiguration() {
        log.debug("[Auto Config] init mybatis plus configuration");
    }

    /**
     * 雪花算法ID生成器
     *
     * @return
     */
    @Bean
    public IdentifierGenerator identifierGenerator(IdGeneratorProperties properties) {
        IdentifierGenerator generator = new SnowflakeIdGenerator();
        SnowflakeIdGenerator.setWorkerId(properties.getWorkerId());
        SnowflakeIdGenerator.setDataCenterId(properties.getDataCenterId());
        log.debug("IdentifierGenerator registry!");
        return generator;
    }

    /**
     * Mybatis plus 公共字段注入
     *
     * @return
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        log.debug("MetaObjectHandler registry!");

        return new MybatisPlusMetaObjectHandler();
    }

    /**
     * MyBatis 分页插件，不注入分页查询没有total数据
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.debug("PaginationInterceptor registry!");

        PaginationInterceptor interceptor = new PaginationInterceptor();
        interceptor.setOverflow(true);
        interceptor.setLimit(100L);
        interceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return interceptor;
    }

}
