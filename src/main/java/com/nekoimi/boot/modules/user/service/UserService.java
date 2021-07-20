package com.nekoimi.boot.modules.user.service;

import com.nekoimi.boot.framework.mybatis.service.BaseService;
import com.nekoimi.boot.modules.user.data.LoginResult;
import com.nekoimi.boot.modules.user.entity.User;

/**
 * User Service
 *
 * nekoimi  2021-07-20
 */
public interface UserService extends BaseService<User> {
    /**
     * Example
     * @param account
     * @param password
     * @return
     */
    LoginResult login(String account, String password);
}
