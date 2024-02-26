package com.rightpair.infra.security;

public interface AuthUserDetailsAdapter {
    AuthUserDetails convertToAuthUserDetails(String username);
}
