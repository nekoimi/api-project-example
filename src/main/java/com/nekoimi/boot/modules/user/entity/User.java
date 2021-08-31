package com.nekoimi.boot.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.nekoimi.boot.framework.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User Entity
 *
 * nekoimi  2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(staticName = "of")
@TableName(value = "user", autoResultMap = true)
public class User extends BaseEntity {
    private static final long serialVersionUID=1L;

    public static final String MOBILE ="mobile";
    public static final String PASSWORD ="password";

    // 手机号
    private String mobile;
    // 密码
    private String password;


}
