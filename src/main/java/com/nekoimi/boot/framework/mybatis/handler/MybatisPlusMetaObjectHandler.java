package com.nekoimi.boot.framework.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * nekoimi  2021/7/2 下午3:40
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        this.setFieldValByName("createdAt", LocalDateTime.now(zoneId), metaObject);
        this.setFieldValByName("updatedAt", LocalDateTime.now(zoneId), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        this.setFieldValByName("updatedAt", LocalDateTime.now(zoneId), metaObject);
    }
}
