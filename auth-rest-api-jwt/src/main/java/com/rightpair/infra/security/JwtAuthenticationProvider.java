package com.rightpair.infra.security;

import com.rightpair.domain.users.entity.User;
import com.rightpair.infra.jwt.dto.JwtPayload;
import com.rightpair.infra.jwt.exception.auth.JwtInvalidAccessTokenException;
import com.rightpair.infra.jwt.service.JwtService;
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
        User user = (User) authUserDetailsService.loadUserByUsername(jwtPayload.email());
        AuthUserDetails authUserDetails =  AuthUserDetails.from(user);
        return JwtAuthenticationToken.authenticated(authUserDetails, accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
