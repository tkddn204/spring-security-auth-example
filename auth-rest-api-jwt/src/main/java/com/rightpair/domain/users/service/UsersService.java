package com.rightpair.domain.users.service;

import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterEmailCheckRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.entity.Role;
import com.rightpair.domain.users.entity.User;
import com.rightpair.domain.users.entity.types.RolesType;
import com.rightpair.domain.users.exception.UserAlreadyExistedException;
import com.rightpair.domain.users.exception.UserNotFoundException;
import com.rightpair.domain.users.exception.UserRoleNotFoundException;
import com.rightpair.domain.users.exception.UserWrongPasswordException;
import com.rightpair.domain.users.repository.RoleRepository;
import com.rightpair.domain.users.repository.UserRepository;
import com.rightpair.global.jwt.dto.JwtPair;
import com.rightpair.global.jwt.dto.JwtPayload;
import com.rightpair.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {
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
        Role role = roleRepository.findByRolesType(RolesType.ASSOCIATE_USER).orElseThrow(UserRoleNotFoundException::new);
        userRepository.save(
                new User(registerRequest.email(),
                        passwordEncoder.encode(registerRequest.password()),
                        registerRequest.name(),
                        role)
        );
    }

    @Transactional(readOnly = true)
    public void emailCheck(UserRegisterEmailCheckRequest emailCheckRequest) {
        validateMemberEmail(emailCheckRequest.email());
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
