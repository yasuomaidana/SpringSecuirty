package com.vulpux.security.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.vulpux.security.security.ApplicationUserPermission.*;

@AllArgsConstructor @Getter
public enum ApplicationUserRole {
    STUDENT(newHashSet()),
    ADMIN(newHashSet(COURSE_READ,COURSE_WRITE,STUDENT_READ,STUDENT_WRITE)),
    ADMIN_TRAINEE(newHashSet(COURSE_READ,STUDENT_READ));

    private final Set<ApplicationUserPermission> permissions;

}
