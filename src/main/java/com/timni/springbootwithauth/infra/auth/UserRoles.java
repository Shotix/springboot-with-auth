package com.timni.springbootwithauth.infra.auth;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoles implements GrantedAuthority {
    ADMINISTRATOR,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
