package com.nekoimi.boot.framework.contract;

/**
 * @author Nekoimi  2020/7/12 22:45
 */
public interface PasswordEncoder {
    String encrypt(String raw);
    String decrypt(String code);
    boolean matches(String raw, String code);
}
