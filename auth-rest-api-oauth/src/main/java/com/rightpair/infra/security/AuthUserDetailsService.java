package com.rightpair.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUserDetailsService implements UserDetailsService {
    private final AuthUserDetailsAdapter authUserDetailsConverter;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authUserDetailsConverter.convertToAuthUserDetails(email);
    }
}
