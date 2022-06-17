package security.models.users;

import lombok.*;
import security.config.security.ApplicationUserPermission;
import security.config.security.ApplicationUserRole;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.AUTO;

@Entity @Data @Builder @AllArgsConstructor @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new HashSet<>();

    @Convert(converter = PermissionsConverter.class)
    private Set<ApplicationUserPermission> permissions;

    public Set<ApplicationUserRole> getRoles(){
        return roles.stream()
                .map(rawRole->ApplicationUserRole
                        .valueOf(rawRole.getName())).collect(Collectors.toSet());
    }

    public Collection<Role> getRolesRaw(){
        return roles;
    }
}

