package com.rightpair.global.security;

import com.rightpair.domain.users.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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

    public AuthUserDetails(Long memberId, String memberEmail, boolean enabled, List<GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static AuthUserDetails from(User user) {
        List<String> roles = user.getUserRoles().stream()
                .map(memberRole -> memberRole.getRole().getRoleType().getValue())
                .toList();
        return new AuthUserDetails(
                user.getId(),
                user.getEmail(),
                !user.isDeleted(),
                AuthorityUtils.createAuthorityList(roles)
        );
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
