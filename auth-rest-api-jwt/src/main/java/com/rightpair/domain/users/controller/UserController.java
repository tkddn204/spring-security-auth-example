package com.rightpair.domain.users.controller;

import com.rightpair.domain.users.controller.api.UserApi;
import com.rightpair.domain.users.dto.request.RefreshAccessTokenRequest;
import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.dto.response.UserRefreshAccessTokenResponse;
import com.rightpair.domain.users.service.UsersService;
import com.rightpair.global.security.AuthUserDetails;
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
    private final UsersService usersService;

    @Override
    public ResponseEntity<UserLoginResponse> authenticateUser(UserAuthenticateRequest authenticateRequest) {
        return ResponseEntity.ok(usersService.authenticate(authenticateRequest));
    }

    @Override
    public ResponseEntity<Void> registerUser(UserRegisterRequest registerRequest) {
        usersService.register(registerRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @Override
    public ResponseEntity<Void> logout(AuthUserDetails authUserDetails, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7).trim();
        usersService.logout(authUserDetails.getMemberEmail(), accessToken);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserRefreshAccessTokenResponse> refreshAccessToken(@Valid @RequestBody RefreshAccessTokenRequest request) {
        return ResponseEntity.ok(
                UserRefreshAccessTokenResponse.from(usersService.refreshAccessToken(request.refreshToken()))
        );
    }
}
