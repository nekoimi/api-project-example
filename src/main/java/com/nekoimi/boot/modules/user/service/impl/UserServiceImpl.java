package com.nekoimi.boot.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nekoimi.boot.framework.contract.jwt.JWTService;
import com.nekoimi.boot.framework.contract.jwt.JWTSubject;
import com.nekoimi.boot.framework.contract.jwt.JWTSubjectService;
import com.nekoimi.boot.framework.error.exception.FailedToNotFoundErrorException;
import com.nekoimi.boot.framework.mybatis.service.impl.BaseServiceImpl;
import com.nekoimi.boot.modules.user.data.LoginResult;
import com.nekoimi.boot.modules.user.entity.User;
import com.nekoimi.boot.modules.user.mapper.UserMapper;
import com.nekoimi.boot.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User Service
 *
 * nekoimi  2021-07-20
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService, JWTSubjectService {
    @Autowired
    private JWTService jwtService;

    @Override
    public LoginResult login(String account, String password) {
        User user = getByOrFail(new LambdaQueryWrapper<User>().eq(User::getMobile, account));
        // TODO pwd Ignore
        String token = jwtService.encode(user);
        return new LoginResult(token, user);
    }

    @Override
    public JWTSubject loadUserById(String subId) throws FailedToNotFoundErrorException {
        return getByOrFail(subId);
    }
}
