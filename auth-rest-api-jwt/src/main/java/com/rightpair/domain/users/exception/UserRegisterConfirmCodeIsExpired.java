package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class UserRegisterConfirmCodeIsExpired extends BusinessException {
    public UserRegisterConfirmCodeIsExpired() {
        super(ErrorCode.NOT_MATCHED_REGISTER_CONFIRM_CODE);
    }
}
