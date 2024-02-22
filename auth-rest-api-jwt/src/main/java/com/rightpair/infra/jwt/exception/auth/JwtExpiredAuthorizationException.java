package com.rightpair.infra.jwt.exception.auth;

import com.rightpair.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtExpiredAuthorizationException extends JwtAuthenticationException {
    public JwtExpiredAuthorizationException() {
        super(ErrorCode.JWT_EXPIRED_AUTHORIZATION);
    }
}
