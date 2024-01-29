package com.rightpair.controller;

import com.rightpair.common.MockMvcUnitTest;
import com.rightpair.common.UsersUtil;
import com.rightpair.entity.Users;
import com.rightpair.security.WithMockAppUser;
import com.rightpair.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
            actions
                    .andExpect(status().isOk())
                    .andExpect(view().name("login-form"));
        }

        @DisplayName("회원가입 페이지에 접속할 수 있다.")
        @Test
        void signUpPage_success() throws Exception {
            // given

            // when
            ResultActions actions = mockMvc.perform(get(USERS_URI_PREFIX + "/signup"));

            // then
            actions
                    .andExpect(status().isOk())
                    .andExpect(view().name("signup-form"));
        }

        @DisplayName("나의 정보 페이지에 접속할 수 있다.")
        @Test
        @WithMockAppUser
        void infoMePage_success() throws Exception {
            // given
            Users users = UsersUtil.getMockedUsersFromSecurityContext();
            given(usersService.getById(anyLong())).willReturn(users);

            // when
            ResultActions actions = mockMvc.perform(get(USERS_URI_PREFIX + "/info/me"));

            // then
            actions.andExpect(status().isOk())
                    .andExpect(model().attribute("user", instanceOf(Users.class)))
                    .andExpect(view().name("user-info"));
        }

        @DisplayName("유저 수정 페이지에 접속할 수 있다.")
        @Test
        @WithMockAppUser
        void updatePage_success() throws Exception {
            // given
            Users users = UsersUtil.getMockedUsersFromSecurityContext();
            given(usersService.getById(anyLong())).willReturn(users);

            // when
            ResultActions actions = mockMvc.perform(get(USERS_URI_PREFIX + "/update"));

            // then
            actions.andExpect(status().isOk())
                    .andExpect(model().attribute("user", instanceOf(Users.class)))
                    .andExpect(view().name("user-update"));
        }
    }

    @DisplayName("Post - 페이지 테스트")
    @Nested
    class PostFormPageTest {

        @DisplayName("사용자가 회원가입 요청을 보내면 회원가입을 할 수 있다.")
        @Test
        void signUp_success() throws Exception {
            // given
            MultiValueMap<String, String> userParams = UsersUtil.getMockedUserParams();

            // when
            ResultActions actions = mockMvc.perform(post(USERS_URI_PREFIX + "/signup")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .params(userParams)
            );

            // then
            actions
                    .andExpect(redirectedUrl(USERS_URI_PREFIX + "/login"))
                    .andExpect(status().is3xxRedirection());
            verify(usersService).create(any(Users.class));
        }

        @DisplayName("사용자가 유효하지 않은 값으로 회원가입 요청을 보내면 회원가입을 할 수 없다.")
        @Test
        void bindingResultError_fail() throws Exception {
            // given
            Users users = new Users("", "", "");
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("username", users.getEmail());
            params.add("password", users.getPassword());
            params.add("nickname", users.getNickname());

            // when
            ResultActions actions = mockMvc.perform(post(USERS_URI_PREFIX + "/signup")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .params(params)
            );

            // then
            actions
                    .andExpect(redirectedUrl(USERS_URI_PREFIX + "/signup"))
                    .andExpect(status().is3xxRedirection());
            verify(usersService, times(0)).create(any(Users.class));
        }

        @DisplayName("사용자가 삭제 요청을 보내면 삭제를 할 수 있다.")
        @Test
        @WithMockAppUser
        void delete_success() throws Exception {
            // given
            // when
            ResultActions actions = mockMvc.perform(post(USERS_URI_PREFIX + "/info/me/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            );

            // then
            actions
                    .andExpect(redirectedUrl(USERS_URI_PREFIX + "/logout"))
                    .andExpect(status().is3xxRedirection());
            verify(usersService).delete(anyLong());
        }
    }

    @DisplayName("사용자가 회원 정보 수정 요청을 보내면 회원 정보를 수정할 수 있다.")
    @Test
    @WithMockAppUser
    void update_success() throws Exception {
        // given
        MultiValueMap<String, String> updatedUserParams = UsersUtil.getMockedUserParams();

        // when
        ResultActions actions = mockMvc.perform(post(USERS_URI_PREFIX + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(updatedUserParams)
        );

        // then
        actions
                .andExpect(redirectedUrl(USERS_URI_PREFIX + "/info/me"))
                .andExpect(status().is3xxRedirection());
        verify(usersService).updateNickName(anyLong(), any(Users.class));
    }

    @DisplayName("사용자가 유효하지 않은 값으로 회원 정보 수정 요청을 보내면 회원 정보를 수정할 수 없다.")
    @Test
    @WithMockAppUser
    void update_fail() throws Exception {
        // given
        Users updatedUsers = new Users("", "", "");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", updatedUsers.getEmail());
        params.add("password", updatedUsers.getPassword());
        params.add("nickname", updatedUsers.getNickname());

        // when
        ResultActions actions = mockMvc.perform(post(USERS_URI_PREFIX + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
        );

        // then
        actions
                .andExpect(redirectedUrl(USERS_URI_PREFIX + "/update"))
                .andExpect(status().is3xxRedirection());
        verify(usersService, times(0)).updateNickName(anyLong(), any(Users.class));
    }
}