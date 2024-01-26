package com.rightpair.controller;

import com.rightpair.common.MockMvcUnitTest;
import com.rightpair.entity.Users;
import com.rightpair.security.AppUserDetails;
import com.rightpair.security.WithMockAppUser;
import com.rightpair.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class UsersControllerTest extends MockMvcUnitTest {
    private static final String USERS_URI_PREFIX = "/users";

    @MockBean
    private UsersService usersService;

    @DisplayName("Get - 페이지 테스트")
    @Nested
    class GetPageTest {
        @DisplayName("로그인 페이지에 접속할 수 있다.")
        @Test
        void loginPage_success() throws Exception {
            // given

            // when
            ResultActions actions = mockMvc.perform(get(USERS_URI_PREFIX + "/login"));

            // then
            actions.andExpect(status().isOk());
            String content = actions.andReturn().getResponse().getContentAsString();
            assertThat(content.contains("로그인")).isTrue();
        }

        @DisplayName("회원가입 페이지에 접속할 수 있다.")
        @Test
        void signUpPage_success() throws Exception {
            // given

            // when
            ResultActions actions = mockMvc.perform(get(USERS_URI_PREFIX + "/signup"));

            // then
            actions.andExpect(status().isOk());
            String content = actions.andReturn().getResponse().getContentAsString();
            assertThat(content.contains("회원가입")).isTrue();
        }

        @DisplayName("나의 정보 페이지에 접속할 수 있다.")
        @Test
        @WithMockAppUser
        void infoMePage_success() throws Exception {
            // given
            Users users = getMockedUser();
            given(usersService.getById(anyLong())).willReturn(users);

            // when
            ResultActions actions = mockMvc.perform(get(USERS_URI_PREFIX + "/info/me"));

            // then
            actions.andExpect(status().isOk())
                    .andExpect(model().attribute("user", instanceOf(Users.class)));
            String content = actions.andReturn().getResponse().getContentAsString();
            assertThat(content.contains("users")).isTrue();
        }

        @DisplayName("유저 수정 페이지에 접속할 수 있다.")
        @Test
        @WithMockAppUser
        void updatePage_success() throws Exception {
            // given
            Users users = getMockedUser();
            given(usersService.getById(anyLong())).willReturn(users);

            // when
            ResultActions actions = mockMvc.perform(get(USERS_URI_PREFIX + "/update"));

            // then
            actions.andExpect(status().isOk())
                    .andExpect(model().attribute("user", instanceOf(Users.class)));
            String content = actions.andReturn().getResponse().getContentAsString();
            assertThat(content.contains("수정")).isTrue();
        }

        private Users getMockedUser() {
            if (SecurityContextHolder.getContext() == null) {
                throw new IllegalArgumentException("@WithMockAppUser 어노테이션을 달아야 합니다.");
            }
            AppUserDetails appUserDetails = (AppUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            Users users = new Users(appUserDetails.getNickname(),
                    appUserDetails.getUsername(), appUserDetails.getPassword());
            users.setId(1000L);
            return users;
        }
    }
}