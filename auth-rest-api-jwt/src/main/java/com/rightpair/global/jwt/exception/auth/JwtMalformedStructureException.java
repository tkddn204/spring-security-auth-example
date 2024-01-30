package com.rightpair.global.jwt.exception.auth;

import com.rightpair.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtMalformedStructureException extends JwtAuthenticationException {
    public JwtMalformedStructureException() {
        super(ErrorCode.JWT_MALFORMED_STRUCTURE);
    }
}
