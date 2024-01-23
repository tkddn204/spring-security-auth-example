package com.rightpair.security;

import com.rightpair.entity.Users;
import com.rightpair.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findUsersByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not Found: %s", username)));
        return new AppUserDetails(users);
    }
}
