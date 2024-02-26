package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class UserAlreadyExistedException extends BusinessException {
    public UserAlreadyExistedException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTED);
    }
}
