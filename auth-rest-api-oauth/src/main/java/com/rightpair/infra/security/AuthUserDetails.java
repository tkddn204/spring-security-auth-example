package com.rightpair.infra.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class AuthUserDetails implements UserDetails {
    private final Long memberId;
    private final String memberEmail;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities;

    public AuthUserDetails(Long memberId, String memberEmail, boolean enabled,
                           List<GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.memberEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
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
