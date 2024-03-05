package com.rightpair.domain.users.entity;

import com.rightpair.domain.users.entity.types.UserStateType;
import com.rightpair.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private final UserStateType state = UserStateType.ACTIVE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<UserRole> userRoles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private UserConfirm userConfirm;

    private User(String nickname, String email, String password, Role userRole) {
        this.userRoles.add(new UserRole(this, userRole));
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static User from(String name, String email, String password, Role role) {
        return new User(name, email, password, role);
    }

    public void addUserConfirm(UserConfirm userConfirm) {
        this.userConfirm = userConfirm;
    }

    public boolean isEnabled() {
        return state.equals(UserStateType.ACTIVE);
    }
}
