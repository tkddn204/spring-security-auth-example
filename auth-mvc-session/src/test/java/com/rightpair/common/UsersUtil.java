package com.rightpair.common;

import com.rightpair.entity.Users;
import com.rightpair.security.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class UsersUtil {
    public static Users getMockedUsersFromSecurityContext() {
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

    public static MultiValueMap<String, String> getMockedUserParams() {
        Users users = new Users("test", "test@test.com", "test-password");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", users.getEmail());
        params.add("password", users.getPassword());
        params.add("nickname", users.getNickname());
        return params;
    }

}
