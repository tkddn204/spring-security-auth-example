package com.rightpair.service;

import com.rightpair.entity.User;
import com.rightpair.entity.types.UsersStateType;
import com.rightpair.event.UserUpdateEvent;
import com.rightpair.exception.AlreadySignedUserException;
import com.rightpair.exception.NullReferenceEntityException;
import com.rightpair.exception.UserNotFoundException;
import com.rightpair.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String FAKE_ENCRYPTED_PASSWORD = "fake-encrypted-password";
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;


    @DisplayName("getById(userId)")
    @Nested
    class GetUserByIdTest {

        @DisplayName("올바른 UserId로 사용자를 조회할 수 있다.")
        @Test
        public void _success() {
            // given
            Long userId = 1L;
            User user = new User("test", "test@test.com", "test-password");
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            userService.getById(userId);

            // then
            verify(userRepository).findById(anyLong());
        }

        @DisplayName("userId에 해당하는 사용자를 찾지 못했을 경우 사용자를 조회할 수 없다.")
        @Test
        public void notExistedUserId_fail() {
            // given
            Long userId = 0L;

            // when
            // then
            assertThrows(UserNotFoundException.class, () -> {
                // when
                userService.getById(userId);
            });
        }
    }

    @DisplayName("create(users)")
    @Nested
    class CreateUserTest {

        @DisplayName("올바른 Users 객체로 사용자를 생성할 수 있다.")
        @Test
        public void _success() {
            // given
            User user = new User("test", "test@test.com", "test-password");
            given(passwordEncoder.encode(anyString())).willReturn(FAKE_ENCRYPTED_PASSWORD);
            given(userRepository.existsByEmail(anyString())).willReturn(false);
            given(userRepository.save(any(User.class))).willReturn(user);

            // when
            userService.create(user);

            // then
            verify(userRepository).existsByEmail(anyString());
            verify(userRepository).save(any(User.class));
        }

        @DisplayName("Users 객체가 Null일 경우 사용자를 생성할 수 없다.")
        @Test
        public void usersIsNull_fail() {
            // given
            User user = null;

            // when
            // then
            assertThrows(NullReferenceEntityException.class,
                    () -> userService.create(user));
        }

        @DisplayName("이미 가입된 이메일로 사용자 생성을 시도하면 사용자를 생성할 수 없다.")
        @Test
        public void alreadySignedEmail_fail() {
            // given
            User user = new User("test", "test@test.com", "test-password");
            given(userRepository.existsByEmail(anyString())).willReturn(true);

            // then
            assertThrows(AlreadySignedUserException.class, () -> {
                // when
                userService.create(user);
            });
        }
    }

    @DisplayName("updateNickName(userId, users)")
    @Nested
    class UpdateNickNameTest {

        @DisplayName("올바른 Users 객체로 사용자를 생성할 수 있다.")
        @Test
        public void _success() {
            // given
            long userId = 1L;
            User user = new User("testname", "test@test.com", "test-password");
            User updatedUser = new User("updatedtestname", "test@test.com", "test-password");

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            userService.updateNickName(userId, updatedUser);

            // then
            ArgumentCaptor<UserUpdateEvent> argumentCaptor = ArgumentCaptor.forClass(UserUpdateEvent.class);
            verify(applicationEventPublisher).publishEvent(argumentCaptor.capture());

            UserUpdateEvent publishedEvent = argumentCaptor.getValue();
            assertThat(publishedEvent.getUpdatedUser().getNickname()).isEqualTo(updatedUser.getNickname());
        }

        @DisplayName("Users 객체가 Null일 경우 사용자 정보를 수정할 수 없다.")
        @Test
        public void usersIsNull_fail() {
            // given
            User updatedUser = null;

            // when
            // then
            assertThrows(NullReferenceEntityException.class,
                    () -> userService.updateNickName(0L, updatedUser));
        }


        @DisplayName("가입되지 않은 사용자 정보로 수정을 시도하면 사용자 정보를 수정할 수 없다.")
        @Test
        public void userNotFound_fail() {
            // given
            User updatedUser = new User("test", "test@test.com", "test-password");

            // then
            assertThrows(UserNotFoundException.class, () -> {
                // when
                userService.updateNickName(0L, updatedUser);
            });
        }
    }

    @DisplayName("delete(userId)")
    @Nested
    class DeleteUserTest {

        @DisplayName("올바른 UserId의 사용자를 삭제할 수 있다.")
        @Test
        public void _success() {
            // given
            long userId = 1L;
            User user = new User("test", "test@test.com", "test-password");
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            userService.delete(userId);

            // then
            verify(userRepository).findById(anyLong());
            assertThat(user.getState()).isEqualTo(UsersStateType.DELETED);
        }

        @DisplayName("userId에 해당하는 사용자를 찾지 못했을 경우 사용자를 조회할 수 없다.")
        @Test
        public void notExistedUserId_fail() {
            // given
            Long userId = 0L;

            // when
            // then
            assertThrows(UserNotFoundException.class, () -> {
                // when
                userService.delete(userId);
            });
        }
    }
}