package com.nekoimi.boot.framework.error.exception.token;

import com.nekoimi.boot.framework.error.enums.Errors;
import com.nekoimi.boot.framework.error.exception.BaseRuntimeException;

/**
 * @author Nekoimi  2020/5/30 20:52
 */
public class TokenCannotBeRefreshException extends BaseRuntimeException {
    public TokenCannotBeRefreshException() {
        super(Errors.TOKEN_CANNOT_BE_REFRESH_EXCEPTION);
    }
}
