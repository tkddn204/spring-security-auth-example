package com.rightpair.event;

import com.rightpair.entity.User;
import com.rightpair.security.AppUserDetails;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateEventListener implements ApplicationListener<UserUpdateEvent> {
    @Override
    public void onApplicationEvent(UserUpdateEvent event) {
        User updatedUser = event.getUpdatedUser();
        AppUserDetails newAppUserDetails = new AppUserDetails(updatedUser);
        Authentication newToken = new UsernamePasswordAuthenticationToken(
                newAppUserDetails, null, newAppUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newToken);
    }
}
