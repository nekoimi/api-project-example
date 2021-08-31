package com.nekoimi.boot.framework.contract.jwt;

import reactor.core.publisher.Mono;

/**
 * @author Nekoimi  2020/5/28 上午11:01
 *
 * 需要实现token自定义存储
 */
public interface JWTStorage {
    String TOKEN_BLACK_LIST = ".black.list:";
    String TOKEN_REFRESH = ".refresh:";
    String TOKEN_REFRESHED = ".refreshed:";

    /**
     * 获取Token的刷新期限
     * @param token
     * @return
     */
    Mono<String> getRefresh(String token);

    /**
     * 设置Token刷新期限
     * @param token
     * @param minutes
     */
    Mono<Void> setRefresh(String token, Integer minutes);

    /**
     * 用来判断token是否被刷新过
     * @param token
     * @return
     */
    Mono<String> getRefreshed(String token);

    /**
     * 以旧token为键 新token为value 临时保存旧token已经被刷新的状态
     * @param token
     * @param newToken
     */
    Mono<Void> setRefreshed(String token, String newToken, Integer minutes);

    /**
     * 将旧token加入黑名单
     * @param token
     */
    Mono<Void> black(String token);

    /**
     * 判断token是否被拉黑
     * @param token
     * @return
     */
    Mono<Boolean> isBlack(String token);

    /**
     * 销毁token缓存
     * @param key
     */
    Mono<Void> destroy(String key);

    /**
     * 清空关于token的全部缓存
     */
    Mono<Void> flush();
}
