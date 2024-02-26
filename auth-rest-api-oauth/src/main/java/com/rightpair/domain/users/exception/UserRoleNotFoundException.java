package com.rightpair.domain.users.exception;

import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;

public class UserRoleNotFoundException extends BusinessException {
    public UserRoleNotFoundException() {
        super(ErrorCode.MEMBER_ROLE_NOT_FOUND);
    }
}
