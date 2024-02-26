package com.rightpair.infra.jwt.exception.auth;

import com.rightpair.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtInvalidSignatureException extends JwtAuthenticationException {
    public JwtInvalidSignatureException() {
        super(ErrorCode.JWT_INVALID_SIGNATURE);
    }
}
