package com.nekoimi.boot.generator.service;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.nekoimi.boot.generator.config.GenerateConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2020/7/13 下午5:45
 *
 * 代码生成器
 */
public class GeneratorService {

    public static void execute(GenerateConfig config) {
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(config.getOutputDir());
        // 生成的文件覆盖
        gc.setFileOverride(true);
        // ActiveRecord特性
        gc.setActiveRecord(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        // 关闭缓存
        gc.setEnableCache(false);
        // 自动打开输出目录
        gc.setOpen(false);
        gc.setAuthor(config.getAuthor());
        gc.setSwagger2(false);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(config.getDbType());
        dsc.setDriverName(config.getJdbcDriver());
        dsc.setUrl(config.getJdbcUrl());
        dsc.setUsername(config.getJdbcUserName());
        dsc.setPassword(config.getJdbcPassword());
//        dsc.setTypeConvert(new MySqlTypeConvert() {
//            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//                //将数据库中datetime转换成date
//                if (fieldType.toLowerCase().contains("datetime")) {
//                    return DbColumnType.DATE;
//                }
//                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
//            }
//        });
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(false);
        strategy.setRestControllerStyle(true);
        strategy.setEntityLombokModel(true);
        strategy.setChainModel(false);
        strategy.setEntityColumnConstant(true);
        strategy.setEntityTableFieldAnnotationEnable(false);
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 字段生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 父类公共字段
        strategy.setSuperEntityColumns("id", "created_at", "updated_at", "deleted_at");
        // controller 父类
        strategy.setSuperControllerClass("com.nekoimi.boot.framework.http.BaseController");
        // mapper 父类
        strategy.setSuperMapperClass("com.nekoimi.boot.framework.mybatis.mapper.BaseMapper");
        // 实体父类
        strategy.setSuperEntityClass("com.nekoimi.boot.framework.mybatis.entity.BaseEntity");
        // 接口父类
        strategy.setSuperServiceClass("com.nekoimi.boot.framework.mybatis.service.BaseService");
        // 接口实现类父类
        strategy.setSuperServiceImplClass("com.nekoimi.boot.framework.mybatis.service.impl.BaseServiceImpl");
        // 需要生成的表
        strategy.setInclude(config.getIncludeTables());
        mpg.setStrategy(strategy);


        // 包配置
        PackageConfig pc = new PackageConfig();
        //父包名
        pc.setParent(config.getParentPackage());
        //父包模块名
        pc.setModuleName(config.getModuleName());
        //实体类父包
        pc.setEntity("entity");
        //controller父包
        pc.setController("controller");
        //mapper父包
        pc.setMapper("mapper");
        //xml父包
        pc.setXml("mapper");
        // 接口包名
        pc.setService("service");
        // 接口实现类包名
        pc.setServiceImpl("service.impl");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("apiVersion", config.getApiVersion());
                map.put("routeName", config.getRouteName());
                this.setMap(map);
            }
        };
        mpg.setCfg(cfg);

//        String jsPath = "/templates/api.js.vm";
//        String vuePath = "/templates/index.vue.vm";
//        List<FileOutConfig> focList = new ArrayList<>();
//        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(jsPath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                String path = gc.getOutputDir() + File.separator + pc.getParent().replace(".", File.separator) + File.separator + "js" + File.separator + tableInfo.getEntityName() + ".js";
//                return path;
//            }
//        });
//        focList.add(new FileOutConfig(vuePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                String path = gc.getOutputDir() + File.separator + pc.getParent().replace(".", File.separator) + File.separator + "view" + File.separator + tableInfo.getEntityName() + File.separator + "index.vue";
//                return path;
//            }
//        });
//
//        cfg.setFileOutConfigList(focList);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController("templates/generator/controller.java.vm");
        templateConfig.setEntity("templates/generator/entity.java.vm");
        templateConfig.setMapper("templates/generator/mapper.java.vm");
        templateConfig.setXml("templates/generator/mapper.xml.vm");
        templateConfig.setService("templates/generator/service.java.vm");
        templateConfig.setServiceImpl("templates/generator/serviceimpl.java.vm");
        mpg.setTemplate(templateConfig);

        // 执行生成
        mpg.execute();
    }

}
