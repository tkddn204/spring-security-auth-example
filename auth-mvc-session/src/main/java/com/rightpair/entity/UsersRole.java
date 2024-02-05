package com.rightpair.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersRole {
    @EmbeddedId
    private UsersRoleId usersRoleId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("usersId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("rolesId")
    private Roles roles;

    public UsersRole(User user, Roles roles) {
        this.usersRoleId = new UsersRoleId(user.getId(), roles.getId());
        this.user = user;
        this.roles = roles;
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