package com.rightpair.security;

import com.rightpair.entity.Users;
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

        Users users = new Users(annotation.nickname(), annotation.username(), annotation.password());
        users.setId(1000L);
        AppUserDetails appUserDetails = new AppUserDetails(users);
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(appUserDetails, users.getPassword(),
                List.of(new SimpleGrantedAuthority(annotation.role())));

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
