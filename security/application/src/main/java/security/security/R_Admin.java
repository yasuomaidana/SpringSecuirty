package security.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN')")
public @interface R_Admin {
}
