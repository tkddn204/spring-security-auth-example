package com.rightpair.infra.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthUserDetailsRepository<T extends UserDetails> {
    Optional<T> findUsersByEmail(String email);
}
