package com.nekoimi.boot.framework.contract.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author Nekoimi  2020/5/28 0:04
 *
 */
public interface JWTService {

    /**
     * 生成新Token
     * @param sub
     * @return
     */
    String encode(JWTSubject sub);

    /**
     * 解码token 返回Token 中的 sub [一般是用户ID]
     * @param token
     * @return
     */
    String decode(String token);

    /**
     * 刷新token 使用一个在刷新期限之内的旧Token获取一个新Token [无痛刷新]
     * @param token
     * @return
     */
    String refresh(String token);

    /**
     * 解析Token 获取Token基本信息
     * @param token
     * @return
     */
    DecodedJWT parse(String token, boolean isCheck);

    /**
     * 清除所有缓存
     */
    void flush();

}
