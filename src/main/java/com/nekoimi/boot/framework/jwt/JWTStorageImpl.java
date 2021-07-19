package com.nekoimi.boot.framework.jwt;

import com.nekoimi.boot.framework.contract.RedisOperator;
import com.nekoimi.boot.framework.contract.jwt.JWTStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Nekoimi  2020/5/28 上午11:15
 */
@Component
public class JWTStorageImpl implements JWTStorage {

    @Autowired
    private RedisOperator<Object> redisOperator;

    @Override
    public String getRefresh(String token) {
        return (String) redisOperator.get(TOKEN_REFRESH_PREFIX + token);
    }

    @Override
    public void setRefresh(String token, Integer minutes) {
        redisOperator.set(TOKEN_REFRESH_PREFIX + token, token, minutes * 60);
    }

    @Override
    public String getRefreshed(String token) {
        return (String) redisOperator.get(TOKEN_REFRESHED_PREFIX + token);
    }

    @Override
    public void setRefreshed(String token, String newToken, Integer minutes) {
        redisOperator.set(TOKEN_REFRESHED_PREFIX + token, newToken, minutes * 60);
    }

    @Override
    public void black(String token) {
        redisOperator.sSet(TOKEN_BLACK_LIST, token);
    }

    @Override
    public boolean isBlack(String token) {
        return redisOperator.sExists(TOKEN_BLACK_LIST, token);
    }

    @Override
    public void destroy(String key) {
        redisOperator.delete(TOKEN_REFRESH_PREFIX + key);
    }

    @Override
    public void flush() {
        // TODO Ignore
    }
}
