package security.models.users;

import lombok.*;
import security.config.security.ApplicationUserPermission;
import security.config.security.ApplicationUserRole;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity @Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;


    @Convert(converter = RolesConverter.class)
    private Set<ApplicationUserRole> roles;

    @Convert(converter = PermissionsConverter.class)
    private Set<ApplicationUserPermission> permissions;

    private boolean accountNonExpired,accountNonLocked,credentialsNonExpired,enabled;
}

