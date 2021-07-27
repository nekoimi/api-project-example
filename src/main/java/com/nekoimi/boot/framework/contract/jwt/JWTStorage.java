package com.nekoimi.boot.framework.contract.jwt;

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
    String getRefresh(String token);

    /**
     * 设置Token刷新期限
     * @param token
     * @param minutes
     */
    void setRefresh(String token, Integer minutes);

    /**
     * 用来判断token是否被刷新过
     * @param token
     * @return
     */
    String getRefreshed(String token);

    /**
     * 以旧token为键 新token为value 临时保存旧token已经被刷新的状态
     * @param token
     * @param newToken
     */
    void setRefreshed(String token, String newToken, Integer minutes);

    /**
     * 将旧token加入黑名单
     * @param token
     */
    void black(String token);

    /**
     * 判断token是否被拉黑
     * @param token
     * @return
     */
    boolean isBlack(String token);

    /**
     * 销毁token缓存
     * @param key
     */
    void destroy(String key);

    /**
     * 清空关于token的全部缓存
     */
    void flush();
}
