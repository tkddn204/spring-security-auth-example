package com.rightpair.infra.converter;

import com.rightpair.domain.users.entity.User;
import com.rightpair.domain.users.repository.UserDetailsRepository;
import com.rightpair.global.exception.ErrorCode;
import com.rightpair.infra.security.AuthUserDetails;
import com.rightpair.infra.security.AuthUserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUserDetailsConverter implements AuthUserDetailsAdapter {
    private final UserDetailsRepository userDetailsRepository;

    @Override
    public AuthUserDetails convertToAuthUserDetails(String email) {
        User user = userDetailsRepository.findUsersByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
        return new AuthUserDetails(user.getId(), user.getEmail(), user.isEnabled(), user.getAuthorities());
    }
}
