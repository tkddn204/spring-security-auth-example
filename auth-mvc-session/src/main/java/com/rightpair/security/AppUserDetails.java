package com.rightpair.security;

import com.rightpair.entity.User;
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


    public AppUserDetails(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.getState().equals(UsersStateType.ACTIVE);
        this.authorities = AuthorityUtils.createAuthorityList(
                user.getUserRoles().stream()
                        .map(usersRole -> usersRole.getRoles().getRoleType().getValue())
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
