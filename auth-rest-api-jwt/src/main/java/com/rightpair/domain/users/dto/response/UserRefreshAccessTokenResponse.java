package com.rightpair.domain.users.dto.response;

import com.rightpair.global.jwt.dto.JwtPair;

public record UserRefreshAccessTokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static UserRefreshAccessTokenResponse from(JwtPair jwtPair) {
        return new UserRefreshAccessTokenResponse(jwtPair.accessToken(), jwtPair.refreshToken(), jwtPair.expiresIn());
    }
}
