package security.models.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import security.config.security.ApplicationUserPermission;
import security.config.security.ApplicationUserRole;

import javax.persistence.*;
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
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions = new HashSet<>();;

    public Set<ApplicationUserRole> getRoles(){
        if(roles == null) return new HashSet<>();
        return roles.stream()
                .map(rawRole->ApplicationUserRole
                        .valueOf(rawRole.getName())).collect(Collectors.toSet());
    }

    public Set<ApplicationUserPermission> getPermissions(){
        if(permissions == null) return new HashSet<>();
        return permissions.stream()
                .map(rawRole->ApplicationUserPermission
                        .valueOf(rawRole.getName())).collect(Collectors.toSet());
    }

    public Set<Role> getRolesRaw(){
        return roles;
    }

    public Set<Permission> getPermissionsRaw(){
        return permissions;
    }

    public Set<? extends GrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        getRoles().forEach(role -> authorities.addAll(role.getGrantedAuthorities()));

        getPermissions()
                    .forEach(permission ->
                            authorities.add(new SimpleGrantedAuthority(permission.getPermission())));

        return authorities;
    }
}

