package com.rightpair.global.security;

import com.rightpair.global.jwt.dto.JwtPayload;
import com.rightpair.global.jwt.exception.auth.JwtInvalidAccessTokenException;
import com.rightpair.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AuthUserDetailsService authUserDetailsService;
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        if (jwtService.isAccessTokenBlackListed(accessToken)) {
            throw new JwtInvalidAccessTokenException();
        }
        JwtPayload jwtPayload = jwtService.verifyToken(accessToken);
        AuthUserDetails authUserDetails = (AuthUserDetails) authUserDetailsService.loadUserByUsername(jwtPayload.email());
        return JwtAuthenticationToken.authenticated(authUserDetails, accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
