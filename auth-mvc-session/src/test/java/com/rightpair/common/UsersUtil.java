package com.rightpair.common;

import com.rightpair.entity.Users;
import com.rightpair.security.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

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
}
