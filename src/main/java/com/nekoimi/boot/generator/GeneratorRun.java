package com.nekoimi.boot.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.nekoimi.boot.generator.config.GenerateConfig;
import com.nekoimi.boot.generator.config.ItemConfig;
import com.nekoimi.boot.generator.service.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/6/13 下午5:47
 */
public class GeneratorRun {
    private final static Logger logger = LoggerFactory.getLogger(GeneratorRun.class);

    public void run(ItemConfig cfg) {
        GenerateConfig config = new GenerateConfig();
        config.setDbType(DbType.getDbType("mysql"));
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test_example?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true");
        config.setJdbcUserName("root");
        config.setJdbcPassword("123456");
        config.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        config.setParentPackage("com.nekoimi.boot.modules");
        config.setOutputDir("/home/nekoimi/Developer/java_projects/Nekoimi/java-project-template-tomcat/java-project-template-for-tomcat/src/main/java");
        config.setAuthor("nekoimi");
        config.setApiVersion("v1");

        // Custom ...
        config.setModuleName(cfg.getModuleName());
        config.setRouteName(cfg.getRouteName());
        config.setIncludeTables(new String[]{cfg.getTableName()});

        GeneratorService.execute(config);
    }

    public static void main(String[] args) {
        GeneratorRun generator = new GeneratorRun();
        List<ItemConfig> configList = new ArrayList<>();
        // 用户模块
        configList.add(new ItemConfig("user", "user", "user"));
        // 快速创建CRUD代码
        for (ItemConfig config : configList) {
            generator.run(config);
            logger.debug("Gen model {} success!", config.getTableName());
        }
    }
}
