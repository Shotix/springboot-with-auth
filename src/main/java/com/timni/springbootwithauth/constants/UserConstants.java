package com.timni.springbootwithauth.constants;

import com.timni.springbootwithauth.infra.auth.UserRoles;

import java.util.Collections;
import java.util.Set;

public class UserConstants {
    public static Set<UserRoles> DEFAULT_ROLES = Collections.singleton(UserRoles.USER);
}
