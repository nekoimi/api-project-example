package com.nekoimi.boot.framework.jwt;

import com.nekoimi.boot.framework.config.properties.AppProperties;
import com.nekoimi.boot.framework.contract.RedisOperator;
import com.nekoimi.boot.framework.contract.jwt.JWTStorage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Nekoimi  2020/5/28 上午11:15
 */
@Component
public class JWTStorageImpl implements JWTStorage {
    private final RedisOperator<Object> redisOperator;
    private final String storagePrefix;

    public JWTStorageImpl(AppProperties appProperties,
                          RedisOperator<Object> redisOperator) {
        this.redisOperator = redisOperator;
        this.storagePrefix = appProperties.getAppKey();
    }

    @Override
    public Mono<String> getRefresh(String token) {
        return redisOperator.getAsString(storagePrefix + TOKEN_REFRESH + token);
    }

    @Override
    public Mono<Void> setRefresh(String token, Integer minutes) {
        return redisOperator.set(storagePrefix + TOKEN_REFRESH + token, token, minutes * 60).then();
    }

    @Override
    public Mono<String> getRefreshed(String token) {
        return redisOperator.getAsString(storagePrefix + TOKEN_REFRESHED + token);
    }

    @Override
    public Mono<Void> setRefreshed(String token, String newToken, Integer minutes) {
        return redisOperator.set(storagePrefix + TOKEN_REFRESHED + token, newToken, minutes * 60).then();
    }

    @Override
    public Mono<Void> black(String token) {
        return redisOperator.sSet(storagePrefix + TOKEN_BLACK_LIST, token).then();
    }

    @Override
    public Mono<Boolean> isBlack(String token) {
        return redisOperator.sExists(storagePrefix + TOKEN_BLACK_LIST, token);
    }

    @Override
    public Mono<Void> destroy(String key) {
        return redisOperator.delete(storagePrefix + TOKEN_REFRESH + key).then();
    }

    @Override
    public Mono<Void> flush() {
        // TODO Ignore
        return Mono.empty().then();
    }
}
