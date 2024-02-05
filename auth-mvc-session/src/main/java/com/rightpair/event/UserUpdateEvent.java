package com.rightpair.event;

import com.rightpair.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserUpdateEvent extends ApplicationEvent {

    private final User updatedUser;

    public UserUpdateEvent(Object source, User updatedUser) {
        super(source);
        this.updatedUser = updatedUser;
    }
}
