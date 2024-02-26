package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
