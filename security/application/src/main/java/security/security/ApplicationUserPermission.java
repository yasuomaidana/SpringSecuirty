package security.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import security.security.annotations.PermissionApplication;

@AllArgsConstructor @Getter @PermissionApplication()
public enum ApplicationUserPermission {
    @PermissionApplication("student:read")
    STUDENT_READ("student:read"),
    @PermissionApplication("student:write")
    STUDENT_WRITE("student:write"),
    @PermissionApplication("course:read")
    COURSE_READ("course:read"),
    @PermissionApplication("course:write")
    COURSE_WRITE("course:write");

    private final String permission;

}
