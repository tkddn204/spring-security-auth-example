package com.rightpair.global.jwt.exception;

import com.rightpair.global.exception.ErrorCode;
import com.rightpair.global.jwt.exception.auth.JwtAuthenticationException;
import lombok.Getter;

@Getter
public class InvalidAuthorizationHeaderException extends JwtAuthenticationException {
    public InvalidAuthorizationHeaderException() {
        super(ErrorCode.INVALID_AUTHORIZATION_HEADER);
    }
}
