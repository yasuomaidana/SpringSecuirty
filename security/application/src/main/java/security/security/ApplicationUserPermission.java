package security.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter @PermissionApplication
public enum ApplicationUserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String permission;

}
