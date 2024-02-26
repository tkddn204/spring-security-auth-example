package com.rightpair.infra.jwt.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class JwtInvalidRefreshTokenException extends BusinessException {
    public JwtInvalidRefreshTokenException() {
        super(ErrorCode.JWT_INVALID_REFRESH_TOKEN);
    }
}
