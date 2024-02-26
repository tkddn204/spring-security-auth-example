package com.rightpair.domain.users.controller.api;

import com.rightpair.domain.users.dto.request.RefreshAccessTokenRequest;
import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.dto.response.UserRefreshAccessTokenResponse;
import com.rightpair.infra.security.AuthUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@Tag(name="Auth", description = "사용자 인증 관련 API")
public interface UserApi {
    @Operation(summary = "사용자 인증", description = "사용자 인증을 요청합니다. 성공시 JWT 토큰 페어를 응답합니다.")
    @PostMapping("/authenticate")
    ResponseEntity<UserLoginResponse> authenticateUser(
            @Valid @RequestBody UserAuthenticateRequest authenticateRequest
    );

    @Operation(summary = "사용자 가입", description = "사용자 인증을 위한 사용자 가입을 요청합니다.")
    @PostMapping("/register")
    ResponseEntity<Void> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest
    );

    @Operation(summary = "사용자 로그아웃", description = "사용자 로그아웃을 요청합니다. 서버의 사용자 인증 값을 제거합니다.")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            HttpServletRequest request
    );

    @Operation(summary = "사용자 액세스 토큰 리프레시", description = "사용자의 액세스 토큰을 재발급합니다.")
    @PostMapping("/refresh")
    ResponseEntity<UserRefreshAccessTokenResponse> refreshAccessToken(
            @Valid @RequestBody RefreshAccessTokenRequest request
    );
}
