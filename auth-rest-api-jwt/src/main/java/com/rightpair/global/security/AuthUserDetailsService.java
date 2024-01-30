package com.rightpair.global.security;

import com.rightpair.domain.users.entity.User;
import com.rightpair.domain.users.exception.UserNotFoundException;
import com.rightpair.domain.users.repository.UserRepository;
import com.rightpair.global.exception.BusinessException;
import com.rightpair.global.exception.ErrorCode;
import com.rightpair.global.jwt.exception.auth.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User storedUser = userRepository.findUsersByEmail(email).orElseThrow(UserNotFoundException::new);
            return AuthUserDetails.from(storedUser);
        } catch (BusinessException e) {
            throw new JwtAuthenticationException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
