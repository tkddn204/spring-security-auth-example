package com.rightpair.entity;

import com.rightpair.entity.types.RolesType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RolesType rolesType;

    @OneToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<UsersRole> memberRoles = new ArrayList<>();

    public Roles(RolesType rolesType) {
        this.rolesType = rolesType;
    }
}