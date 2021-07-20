package com.nekoimi.boot.modules.user.data;

import com.nekoimi.boot.modules.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * nekoimi  2021/7/20 下午4:18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {
    private String token;
    private User user;
}
