package com.rightpair.global.jwt.exception.auth;

import com.rightpair.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtUnsupportedFormatException extends JwtAuthenticationException {
    public JwtUnsupportedFormatException() {
        super(ErrorCode.JWT_UNSUPPORTED_FORMAT);
    }
}
