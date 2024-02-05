package com.rightpair.security;

import com.rightpair.entity.User;
import com.rightpair.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUsersByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not Found: %s", username)));
        return new AppUserDetails(user);
    }
}
