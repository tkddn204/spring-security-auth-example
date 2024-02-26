package com.rightpair.domain.users.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {
    @EmbeddedId
    private UsersRoleId usersRoleId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("usersId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("rolesId")
    private Role role;

    public UserRole(User user, Role role) {
        this.usersRoleId = new UsersRoleId(user.getId(), role.getId());
        this.user = user;
        this.role = role;
    }

    @Getter
    @Embeddable
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class UsersRoleId implements Serializable {
        private Long usersId;
        private Long rolesId;
    }
}