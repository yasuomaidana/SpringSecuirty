package com.vulpux.security.security;

import security.security.ApplicationUserRole;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Inherited
@Target({METHOD,TYPE})
public @interface RoleAuthorization {
    ApplicationUserRole[] value();
}
