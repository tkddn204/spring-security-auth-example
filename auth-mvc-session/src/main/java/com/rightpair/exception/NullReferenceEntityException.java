package com.rightpair.exception;

public class NullReferenceEntityException extends BusinessException {
    public NullReferenceEntityException(Class<?> clazz) {
        super(ErrorCode.NULL_REFERENCE_ENTITY,
                ErrorCode.NULL_REFERENCE_ENTITY.getMessage().formatted(clazz.getSimpleName()));
    }
}
