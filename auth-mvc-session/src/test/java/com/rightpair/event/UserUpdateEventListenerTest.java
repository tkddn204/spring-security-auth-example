package com.rightpair.event;

import com.rightpair.entity.User;
import com.rightpair.security.AppUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserUpdateEventListenerTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Test
    void updateSecurityContextWhenUserUpdateEventIsPublished_success() {
        // given
        User updatedUser = new User("updatedUser", "test@test.com", "test1234");
        UserUpdateEvent event = new UserUpdateEvent(this, updatedUser);

        // when
        eventPublisher.publishEvent(event);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        assertEquals(updatedUser.getEmail(), userDetails.getUsername());
    }
}