package com.rightpair.domain.users.dto.response;

import com.rightpair.infra.jwt.dto.JwtPair;

public record UserLoginResponse(
        String accessToken,
        String refreshToken,
        String name,
        String email,
        long expiresIn
) {
    public static UserLoginResponse from(String name, String email, JwtPair jwtPair) {
        return new UserLoginResponse(
                jwtPair.accessToken(),
                jwtPair.refreshToken(),
                name,
                email,
                jwtPair.expiresIn());
    }
}
