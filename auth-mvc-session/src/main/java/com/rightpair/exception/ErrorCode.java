package com.rightpair.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NULL_REFERENCE_ENTITY("주어진 객체 레퍼런스가 없습니다 : %s"),
    ALREADY_SIGNED_USER("이미 같은 이메일로 가입된 회원이 있습니다."),
    USER_NOT_FOUND("가입된 회원 정보가 없습니다.")
    ;

    private final String message;
}
