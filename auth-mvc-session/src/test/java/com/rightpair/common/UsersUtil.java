package com.rightpair.common;

import com.rightpair.entity.User;
import com.rightpair.security.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class UsersUtil {
    public static User getMockedUsersFromSecurityContext() {
        if (SecurityContextHolder.getContext() == null) {
            throw new IllegalArgumentException("@WithMockAppUser 어노테이션을 달아야 합니다.");
        }
        AppUserDetails appUserDetails = (AppUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = new User(appUserDetails.getNickname(),
                appUserDetails.getUsername(), appUserDetails.getPassword());
        user.setId(1000L);
        return user;
    }

    public static MultiValueMap<String, String> getMockedUserParams() {
        User user = new User("test", "test@test.com", "test-password");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", user.getEmail());
        params.add("password", user.getPassword());
        params.add("nickname", user.getNickname());
        return params;
    }

}
