package security.security.annotations;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
public @interface PermissionApplication {
    String value() default "";
}
