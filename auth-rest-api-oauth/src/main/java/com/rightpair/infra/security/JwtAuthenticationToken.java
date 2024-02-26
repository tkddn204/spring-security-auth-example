package com.rightpair.infra.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private AuthUserDetails authUserDetails;
    private final String credentials;

    public JwtAuthenticationToken(AuthUserDetails authUserDetails, String credentials, boolean isAuthenticated) {
        super(authUserDetails.getAuthorities());
        this.authUserDetails = authUserDetails;
        this.credentials = credentials;
        setAuthenticated(isAuthenticated);
    }

    public JwtAuthenticationToken(String credentials, boolean isAuthenticated) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.credentials = credentials;
        setAuthenticated(isAuthenticated);
    }

    public static JwtAuthenticationToken unauthenticated(String accessToken) {
        return new JwtAuthenticationToken(accessToken,false);
    }

    public static JwtAuthenticationToken authenticated(AuthUserDetails authUserDetails, String accessToken) {
        return new JwtAuthenticationToken(authUserDetails, accessToken, true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.authUserDetails;
    }
}
