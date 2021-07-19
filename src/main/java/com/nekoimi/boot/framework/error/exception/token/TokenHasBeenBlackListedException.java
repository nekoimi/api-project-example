package com.nekoimi.boot.framework.error.exception.token;


import com.nekoimi.boot.framework.error.enums.Errors;
import com.nekoimi.boot.framework.error.exception.BaseRuntimeException;

/**
 * @author Nekoimi  2020/5/30 20:35
 */
public class TokenHasBeenBlackListedException extends BaseRuntimeException {
    public TokenHasBeenBlackListedException() {
        super(Errors.TOKEN_HAS_BEEN_BLACKLISTED_EXCEPTION);
    }
}
