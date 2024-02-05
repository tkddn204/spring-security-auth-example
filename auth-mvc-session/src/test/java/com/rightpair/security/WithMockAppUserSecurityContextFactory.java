package com.rightpair.security;

import com.rightpair.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockAppUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAppUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockAppUser annotation) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        User user = new User(annotation.nickname(), annotation.username(), annotation.password());
        user.setId(1000L);
        AppUserDetails appUserDetails = new AppUserDetails(user);
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(appUserDetails, user.getPassword(),
                List.of(new SimpleGrantedAuthority(annotation.role())));

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
