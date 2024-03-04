package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class UserRegisterMessageException extends BusinessException {
    private final static ErrorCode ERROR_CODE = ErrorCode.MAIL_ILLEGAL_EXCEPTION;

    public UserRegisterMessageException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
