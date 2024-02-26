package com.rightpair.infra.security;

import java.util.Optional;

public interface AuthUserDetailsRepository<T> {
    Optional<T> findUsersByEmail(String email);
}
