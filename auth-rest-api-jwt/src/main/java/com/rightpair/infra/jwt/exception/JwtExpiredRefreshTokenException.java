package com.rightpair.infra.jwt.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class JwtExpiredRefreshTokenException extends BusinessException {
    public JwtExpiredRefreshTokenException() {
        super(ErrorCode.JWT_EXPIRED_AUTHORIZATION);
    }
}
