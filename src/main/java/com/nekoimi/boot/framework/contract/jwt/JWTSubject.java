package com.nekoimi.boot.framework.contract.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * @author Nekoimi  2020/5/28 0:41
 * <p>
 * 使用 JWT 认证的用户 Entity 需要扩展这个接口
 */
public interface JWTSubject {
    /**
     * 唯一标识，一半是主键
     * @return
     */
    @JsonIgnore
    String getJWTIdentifier();

    /**
     * 自定义参数，k <-> v 形式
     * @return
     */
    @JsonIgnore
    default Map<String, String> getJWTCustomClaims() {
        return null;
    }
}
