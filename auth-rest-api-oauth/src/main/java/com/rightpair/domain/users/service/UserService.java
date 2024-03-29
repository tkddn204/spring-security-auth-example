package com.rightpair.domain.users.service;

import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.entity.Role;
import com.rightpair.domain.users.entity.User;
import com.rightpair.domain.users.entity.types.RoleType;
import com.rightpair.domain.users.exception.UserAlreadyExistedException;
import com.rightpair.domain.users.exception.UserNotFoundException;
import com.rightpair.domain.users.exception.UserRoleNotFoundException;
import com.rightpair.domain.users.exception.UserWrongPasswordException;
import com.rightpair.domain.users.repository.RoleRepository;
import com.rightpair.domain.users.repository.UserRepository;
import com.rightpair.infra.jwt.dto.JwtPair;
import com.rightpair.infra.jwt.dto.JwtPayload;
import com.rightpair.infra.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserLoginResponse authenticate(UserAuthenticateRequest loginRequest) {
        User storedUser = userRepository.findUsersByEmail(loginRequest.email())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginRequest.password(), storedUser.getPassword())) {
            throw new UserWrongPasswordException();
        }

        JwtPair newTokenPair = jwtService.createTokenPair(
                JwtPayload.fromNow(String.valueOf(storedUser.getId()), storedUser.getEmail()));

        return UserLoginResponse.from(storedUser.getNickname(), storedUser.getEmail(), newTokenPair);
    }

    @Transactional
    public void register(UserRegisterRequest registerRequest) {
        validateMemberEmail(registerRequest.email());
        Role role = roleRepository.findByRoleType(RoleType.ASSOCIATE_USER).orElseThrow(UserRoleNotFoundException::new);
        userRepository.save(
                new User(registerRequest.email(),
                        passwordEncoder.encode(registerRequest.password()),
                        registerRequest.name(),
                        role)
        );
    }

    public void logout(String email, String accessToken) {
        jwtService.addAccessTokenToBlackList(email, accessToken);
        jwtService.deleteRefreshToken(email);
    }

    public JwtPair refreshAccessToken(String refreshToken) {
        return jwtService.refreshAccessToken(refreshToken);
    }

    private void validateMemberEmail(String registerRequest) {
        if (userRepository.existsByEmail(registerRequest)) {
            throw new UserAlreadyExistedException();
        }
    }
}
