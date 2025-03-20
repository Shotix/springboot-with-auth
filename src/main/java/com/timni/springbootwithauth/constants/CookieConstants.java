package com.timni.springbootwithauth.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CookieConstants {

    @Value("${cookie.refresh.name}")
    private String refreshTokenCookieName;

    @Value("${cookie.refresh.httpOnly}")
    private boolean refreshTokenCookieHttpOnly;

    @Value("${cookie.refresh.secure}")
    private boolean refreshTokenCookieSecure;

    @Value("${cookie.refresh.maxAge}")
    private int refreshTokenCookieMaxAge;

    @Value("${cookie.refresh.path}")
    private String refreshTokenCookiePath;
}

