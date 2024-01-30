package com.rightpair.global.jwt.exception.auth;

import com.rightpair.global.exception.ErrorCode;

public class JwtInvalidAccessTokenException extends JwtAuthenticationException {
    public JwtInvalidAccessTokenException() {
        super(ErrorCode.JWT_INVALID_ACCESS_TOKEN);
    }
}
