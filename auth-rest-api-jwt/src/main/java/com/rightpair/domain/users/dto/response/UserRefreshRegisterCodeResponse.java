package com.rightpair.domain.users.dto.response;

public record UserRefreshRegisterCodeResponse(
        String code
) {
    public static UserRefreshRegisterCodeResponse from(String code) {
        return new UserRefreshRegisterCodeResponse(code);
    }
}
