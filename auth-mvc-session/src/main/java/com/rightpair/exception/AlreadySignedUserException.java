package com.rightpair.exception;

public class AlreadySignedUserException extends BusinessException {
    public AlreadySignedUserException() {
        super(ErrorCode.ALREADY_SIGNED_USER);
    }
}
