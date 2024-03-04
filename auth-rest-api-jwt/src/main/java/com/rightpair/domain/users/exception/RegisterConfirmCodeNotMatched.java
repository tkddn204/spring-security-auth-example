package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class RegisterConfirmCodeNotMatched extends BusinessException {
    public RegisterConfirmCodeNotMatched() {
        super(ErrorCode.NOT_MATCHED_REGISTER_CONFIRM_CODE);
    }
}
