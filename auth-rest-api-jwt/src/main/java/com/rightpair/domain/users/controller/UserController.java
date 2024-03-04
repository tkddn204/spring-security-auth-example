package com.rightpair.domain.users.controller;

import com.rightpair.domain.users.controller.api.UserApi;
import com.rightpair.domain.users.dto.request.RefreshAccessTokenRequest;
import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.dto.response.UserRefreshAccessTokenResponse;
import com.rightpair.domain.users.dto.response.UserRefreshRegisterCodeResponse;
import com.rightpair.domain.users.service.UserService;
import com.rightpair.infra.security.AuthUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserLoginResponse> authenticateUser(UserAuthenticateRequest authenticateRequest) {
        return ResponseEntity.ok(userService.authenticate(authenticateRequest));
    }

    @Override
    public ResponseEntity<Void> registerUser(UserRegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @Override
    public ResponseEntity<Void> logout(AuthUserDetails authUserDetails, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7).trim();
        userService.logout(authUserDetails.getMemberEmail(), accessToken);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> confirm(String confirmCode) {
        userService.registerConfirm(confirmCode);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserRefreshAccessTokenResponse> refreshAccessToken(@Valid @RequestBody RefreshAccessTokenRequest request) {
        return ResponseEntity.ok(
                UserRefreshAccessTokenResponse.from(userService.refreshAccessToken(request.refreshToken()))
        );
    }

    @Override
    public ResponseEntity<UserRefreshRegisterCodeResponse> refreshRegisterConfirmCode(String email) {
        return ResponseEntity.ok(
                UserRefreshRegisterCodeResponse.from(userService.refreshRegisterConfirmCode(email))
        );
    }
}
