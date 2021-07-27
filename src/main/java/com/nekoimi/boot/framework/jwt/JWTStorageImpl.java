package com.nekoimi.boot.framework.jwt;

import com.nekoimi.boot.framework.configuration.properties.JWTProperties;
import com.nekoimi.boot.framework.contract.RedisOperator;
import com.nekoimi.boot.framework.contract.jwt.JWTStorage;
import org.springframework.stereotype.Component;

/**
 * @author Nekoimi  2020/5/28 上午11:15
 */
@Component
public class JWTStorageImpl implements JWTStorage {
    private final RedisOperator<Object> redisOperator;
    private final String storagePrefix;

    public JWTStorageImpl(RedisOperator<Object> redisOperator,
                          JWTProperties jwtProperties) {
        this.redisOperator = redisOperator;
        this.storagePrefix = jwtProperties.getStoragePrefix();
    }

    @Override
    public String getRefresh(String token) {
        return (String) redisOperator.get(storagePrefix + TOKEN_REFRESH + token);
    }

    @Override
    public void setRefresh(String token, Integer minutes) {
        redisOperator.set(storagePrefix + TOKEN_REFRESH + token, token, minutes * 60);
    }

    @Override
    public String getRefreshed(String token) {
        return (String) redisOperator.get(storagePrefix + TOKEN_REFRESHED + token);
    }

    @Override
    public void setRefreshed(String token, String newToken, Integer minutes) {
        redisOperator.set(storagePrefix + TOKEN_REFRESHED + token, newToken, minutes * 60);
    }

    @Override
    public void black(String token) {
        redisOperator.sSet(storagePrefix + TOKEN_BLACK_LIST, token);
    }

    @Override
    public boolean isBlack(String token) {
        return redisOperator.sExists(storagePrefix + TOKEN_BLACK_LIST, token);
    }

    @Override
    public void destroy(String key) {
        redisOperator.delete(storagePrefix + TOKEN_REFRESH + key);
    }

    @Override
    public void flush() {
        // TODO Ignore
    }
}
