package com.nekoimi.boot.modules.user.mapper;


import com.nekoimi.boot.framework.mybatis.mapper.BaseMapper;
import com.nekoimi.boot.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * User Mapper 接口
 *
 * nekoimi  2021-07-28
 */
@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {
}
