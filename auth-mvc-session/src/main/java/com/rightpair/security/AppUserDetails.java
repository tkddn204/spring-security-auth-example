package com.rightpair.security;

import com.rightpair.entity.Users;
import com.rightpair.entity.types.UsersStateType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
    private final Long id;
    private final String nickname;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities;


    public AppUserDetails(Users users) {
        this.id = users.getId();
        this.nickname = users.getNickname();
        this.username = users.getEmail();
        this.password = users.getPassword();
        this.enabled = users.getState().equals(UsersStateType.ACTIVE);
        this.authorities = AuthorityUtils.createAuthorityList(
                users.getUserRoles().stream()
                        .map(usersRole -> usersRole.getRoles().getRolesType().getValue())
                        .toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
