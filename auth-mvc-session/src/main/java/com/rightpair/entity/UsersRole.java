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
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("rolesId")
    private Roles roles;

    public UsersRole(Users users, Roles roles) {
        this.usersRoleId = new UsersRoleId(users.getId(), roles.getId());
        this.users = users;
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