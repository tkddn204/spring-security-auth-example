package com.rightpair.domain.users.controller;

import com.rightpair.common.IntegrationTest;
import com.rightpair.domain.users.dto.request.RefreshAccessTokenRequest;
import com.rightpair.domain.users.dto.request.UserAuthenticateRequest;
import com.rightpair.domain.users.dto.request.UserRegisterRequest;
import com.rightpair.domain.users.dto.response.UserLoginResponse;
import com.rightpair.domain.users.entity.User;
import com.rightpair.domain.users.entity.UserConfirm;
import com.rightpair.domain.users.entity.types.RoleType;
import com.rightpair.domain.users.repository.RoleRepository;
import com.rightpair.domain.users.repository.UserConfirmRepository;
import com.rightpair.domain.users.repository.UserRepository;
import com.rightpair.infra.mail.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import javax.management.relation.RoleNotFoundException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTest {

    private final static String BASE_URL = "/v1/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConfirmRepository userConfirmRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MailService mailService;

    @DisplayName("사용자 회원가입")
    @Nested
    class RegisterTest {

        @DisplayName("올바른 요청으로 회원가입에 성공")
        @Test
        void __success() throws Exception {
            // given
            UserRegisterRequest registerRequest = new UserRegisterRequest(
                    "tkddn204@gmail.com", "testpassword", "testname"
            );


            // when
            ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest))
            );

            // then
            verify(mailService).sendRegisterConfirmMail(anyString(), anyString(), anyString());
            resultActions
                    .andExpect(status().isCreated());
        }
    }


    @DisplayName("사용자 로그인")
    @Nested
    class LoginTest {

        User storedUser;

        @BeforeEach
        void beforeEach() throws Exception {
            storedUser = userRepository.save(User.from(
                    "testname",
                    "test@test.com",
                    passwordEncoder.encode("test1234"),
                    roleRepository.findByRoleType(RoleType.ASSOCIATE_USER).orElseThrow(RoleNotFoundException::new)
            ));
            userConfirmRepository.save(UserConfirm.from(storedUser));
        }

        @DisplayName("올바른 요청으로 로그인에 성공")
        @Test
        void __success() throws Exception {
            // given
            UserAuthenticateRequest authenticateRequest = new UserAuthenticateRequest(
                    "test@test.com", "test1234"
            );

            // when
            ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authenticateRequest))
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.refreshToken").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.email").exists())
                    .andExpect(jsonPath("$.expiresIn").exists());
        }
    }

    @DisplayName("사용자 로그아웃")
    @Nested
    class LogoutTest {
        @BeforeEach
        void beforeEach() throws Exception {
            User user = userRepository.save(User.from(
                    "testname",
                    "test@test.com",
                    passwordEncoder.encode("test1234"),
                    roleRepository.findByRoleType(RoleType.ASSOCIATE_USER).orElseThrow(RoleNotFoundException::new)
            ));
            userConfirmRepository.save(UserConfirm.from(user));
        }

        @DisplayName("올바른 요청으로 로그아웃에 성공")
        @Test
        void __success() throws Exception {
            // given
            UserAuthenticateRequest authenticateRequest = new UserAuthenticateRequest(
                    "test@test.com", "test1234"
            );
            String loginResponseStr = mockMvc.perform(post(BASE_URL + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authenticateRequest))
                    ).andReturn().getResponse().getContentAsString();
            UserLoginResponse loginResponse = objectMapper.readValue(loginResponseStr, UserLoginResponse.class);

            // when
            ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/logout")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.accessToken())
            );

            // then
            resultActions
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("사용자 액세스 토큰 리프레시")
    @Nested
    class RefreshAccessTokenTest {
        @BeforeEach
        void beforeEach() throws Exception {
            User user = userRepository.save(User.from(
                    "testname",
                    "test@test.com",
                    passwordEncoder.encode("test1234"),
                    roleRepository.findByRoleType(RoleType.ASSOCIATE_USER).orElseThrow(RoleNotFoundException::new)
            ));
            userConfirmRepository.save(UserConfirm.from(user));
        }

        @DisplayName("올바른 요청으로 액세스 토큰 리프레시에 성공")
        @Test
        void __success() throws Exception {
            // given
            UserAuthenticateRequest authenticateRequest = new UserAuthenticateRequest(
                    "test@test.com", "test1234"
            );
            String loginResponseStr = mockMvc.perform(post(BASE_URL + "/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authenticateRequest))
            ).andReturn().getResponse().getContentAsString();
            UserLoginResponse loginResponse = objectMapper.readValue(loginResponseStr, UserLoginResponse.class);
            RefreshAccessTokenRequest request = new RefreshAccessTokenRequest(loginResponse.refreshToken());

            // when
            ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/refresh")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.accessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.refreshToken").exists())
                    .andExpect(jsonPath("$.expiresIn").exists());
        }
    }
}