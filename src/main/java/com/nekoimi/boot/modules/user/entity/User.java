package com.nekoimi.boot.modules.user.entity;

import com.nekoimi.boot.framework.contract.jwt.JWTSubject;
import com.nekoimi.boot.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User Entity
 *
 * nekoimi  2021-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName(autoResultMap = true)
public class User extends BaseEntity implements JWTSubject {
    private static final long serialVersionUID=1L;

    public static final String MOBILE ="mobile";
    public static final String PASSWORD ="password";

    // 手机号
    private String mobile;
    // 密码
    private String password;


    @Override
    public String getJWTIdentifier() {
        return id;
    }
}
