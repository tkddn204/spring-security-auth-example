package com.rightpair.domain.users.entity;

import com.rightpair.domain.users.entity.types.RoleType;
import com.rightpair.domain.users.entity.types.UserStateType;
import com.rightpair.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements UserDetails {
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

    public User(String nickname, String email, String password, Role userRole) {
        this.userRoles.add(new UserRole(this, userRole));
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static User empty() {
        return new User();
    }

    public User encryptPassword(String encryptedPassword) {
        this.password = encryptedPassword;
        return this;
    }

    public User newFaceUser() {
        this.userRoles.add(new UserRole(this, new Role(RoleType.ASSOCIATE_USER)));
        return this;
    }

    public boolean isDeleted() {
        return this.state.equals(UserStateType.DELETED);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(this.userRoles.stream()
                .map(userRole -> userRole.getRole().getRoleType().getValue()).toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !state.equals(UserStateType.SUSPENDED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !state.equals(UserStateType.LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return state.equals(UserStateType.ACTIVE);
    }
}
