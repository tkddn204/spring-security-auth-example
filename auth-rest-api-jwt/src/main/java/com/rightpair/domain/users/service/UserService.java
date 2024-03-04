package com.rightpair.domain.users.service;

import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.entity.Role;
import com.rightpair.domain.users.entity.User;
import com.rightpair.domain.users.entity.UserConfirm;
import com.rightpair.domain.users.entity.UserRole;
import com.rightpair.domain.users.entity.types.RoleType;
import com.rightpair.domain.users.exception.*;
import com.rightpair.domain.users.repository.RoleRepository;
import com.rightpair.domain.users.repository.UserConfirmRepository;
import com.rightpair.domain.users.repository.UserRepository;
import com.rightpair.domain.users.repository.UserRoleRepository;
import com.rightpair.infra.jwt.dto.JwtPair;
import com.rightpair.infra.jwt.dto.JwtPayload;
import com.rightpair.infra.jwt.service.JwtService;
import com.rightpair.infra.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConfirmRepository userConfirmRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public UserLoginResponse authenticate(UserAuthenticateRequest loginRequest) {
        User storedUser = userRepository.findUserByEmail(loginRequest.email())
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
        User user = userRepository.save(
                User.from(registerRequest.name(),
                        registerRequest.email(),
                        passwordEncoder.encode(registerRequest.password()),
                        role));

        mailService.sendRegisterConfirmMail(
                registerRequest.email(),
                registerRequest.name(),
                user.getUserConfirm().getCode()
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

    @Transactional
    public void registerConfirm(String confirmCode) {
        User user = userRepository.findUserByUserConfirmCode(confirmCode)
                .orElseThrow(RegisterConfirmCodeNotMatched::new);
        if (user.getUserConfirm().isExpired()) {
            throw new UserRegisterConfirmCodeIsExpired();
        }

        userRoleRepository.deleteAllByUserIdAndRoleType(user.getId(), RoleType.ASSOCIATE_USER);

        Role role = roleRepository.findByRoleType(RoleType.REGULAR_USER)
                        .orElseThrow(UserRoleNotFoundException::new);
        userRoleRepository.save(new UserRole(user, role));

        user.getUserConfirm().updateCompleted();
    }

    @Transactional
    public String refreshRegisterConfirmCode(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        UserConfirm userConfirm = userConfirmRepository.findByUser(user)
                .orElseThrow();
        if (userConfirm.isExpired()) {
            throw new UserRegisterConfirmCodeIsExpired();
        }

        if (userConfirm.isCompleted()) {
            throw new UserRegisterConfirmAlreadyCompleted();
        }

        userConfirm.refreshCode();

        return userConfirm.getCode();
    }
}
