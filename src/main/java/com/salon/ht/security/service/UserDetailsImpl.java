package com.salon.ht.security.service;

import com.salon.ht.constant.UserStatus;
import com.salon.ht.entity.Permission;
import com.salon.ht.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsImpl extends UserEntity implements UserDetails {

    public UserDetailsImpl(final UserEntity userEntity) {
        super(userEntity);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> permissions;
        permissions = getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
        getRoles().forEach(role -> {
            Set<String> permissionsOfRoles = role.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet());
            permissions.addAll(permissionsOfRoles);
        });
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        LOGGER.info("user status {}", getStatus());
        return !super.getStatus().equals(UserStatus.LOCK);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !super.getStatus().equals(UserStatus.LOCK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsImpl)) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(getId(), that.getId());
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(UserDetailsImpl.class);
}
