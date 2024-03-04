package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class UserRegisterConfirmAlreadyCompleted extends BusinessException {
    public UserRegisterConfirmAlreadyCompleted() {
        super(ErrorCode.REGISTER_CONFIRM_ALREADY_COMPLETED);
    }
}
