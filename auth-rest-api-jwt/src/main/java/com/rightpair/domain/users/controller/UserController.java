package com.rightpair.domain.users.controller;

import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterEmailCheckRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.request.RefreshAccessTokenRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.dto.response.UserRefreshAccessTokenResponse;
import com.rightpair.domain.users.service.UsersService;
import com.rightpair.global.security.AuthUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @PostMapping("/authenticate")
    public ResponseEntity<UserLoginResponse> authenticateUser(
            @Valid @RequestBody UserAuthenticateRequest authenticateRequest
    ) {
        return ResponseEntity.ok(usersService.authenticate(authenticateRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest
    ) {
        usersService.register(registerRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/register/check/email")
    public ResponseEntity<Void> memberEmailCheck(
            @Valid UserRegisterEmailCheckRequest emailCheckRequest
    ) {
        usersService.emailCheck(emailCheckRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            HttpServletRequest request
    ) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7).trim();
        usersService.logout(authUserDetails.getMemberEmail(), accessToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserRefreshAccessTokenResponse> refreshAccessToken(
            @Valid @RequestBody RefreshAccessTokenRequest request
    ) {
        return ResponseEntity.ok(
                UserRefreshAccessTokenResponse.from(usersService.refreshAccessToken(request.refreshToken()))
        );
    }
}
