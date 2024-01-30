package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class UserWrongPasswordException extends BusinessException {
    public UserWrongPasswordException() {
        super(ErrorCode.MEMBER_WRONG_PASSWORD);
    }
}
