package com.rightpair.domain.users.entity.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ASSOCIATE_USER("ROLE_ASSOCIATE_USER"),
    REGULAR_USER("ROLE_REGULAR_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}
