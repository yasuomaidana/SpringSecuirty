package security.auth;

import lombok.*;
import security.security.ApplicationUserPermission;
import security.security.ApplicationUserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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


    @ElementCollection
    @CollectionTable( name = "user_roles",
            joinColumns = @JoinColumn( name = "user_id" ) )
    @Column( name = "roles" )
    @Enumerated(EnumType.STRING)
    @Convert( converter = RolesConverter.class )
    private List<ApplicationUserRole> roles;

    @ElementCollection
    @CollectionTable( name = "user_permissions",
            joinColumns = @JoinColumn( name = "user_id" ) )
    @Column( name = "permissions" )
    @Enumerated(EnumType.STRING)
    @Convert(converter = PermissionsConverter.class)
    private List<ApplicationUserPermission> permissions;

    private boolean accountNonExpired,accountNonLocked,credentialsNonExpired,enabled;
}

@Converter
class RolesConverter implements AttributeConverter<List<ApplicationUserRole>,List<String>>{

    @Override
    public List<String> convertToDatabaseColumn(List<ApplicationUserRole> roles) {
        return roles.stream().map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationUserRole> convertToEntityAttribute(List<String> rolesEncoded) {

        return rolesEncoded.stream().map(ApplicationUserRole::valueOf).collect(Collectors.toList());
    }
}

@Converter
class PermissionsConverter implements AttributeConverter<List<ApplicationUserPermission>,List<String>>{

    @Override
    public List<String> convertToDatabaseColumn(List<ApplicationUserPermission> permissions) {
        return permissions.stream().map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationUserPermission> convertToEntityAttribute(List<String> permissionsEncoded) {
        return permissionsEncoded.stream().map(ApplicationUserPermission::valueOf).collect(Collectors.toList());
    }
}