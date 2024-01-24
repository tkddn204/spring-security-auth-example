package com.rightpair.event;

import com.rightpair.entity.Users;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserUpdateEvent extends ApplicationEvent {

    private final Users updatedUser;

    public UserUpdateEvent(Object source, Users updatedUser) {
        super(source);
        this.updatedUser = updatedUser;
    }
}
