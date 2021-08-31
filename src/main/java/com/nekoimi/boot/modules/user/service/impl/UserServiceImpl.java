package com.nekoimi.boot.modules.user.service.impl;

import com.nekoimi.boot.framework.mybatis.service.impl.BaseServiceImpl;
import com.nekoimi.boot.modules.user.entity.User;
import com.nekoimi.boot.modules.user.mapper.UserMapper;
import com.nekoimi.boot.modules.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * User Service
 *
 * nekoimi  2021-07-28
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
}
