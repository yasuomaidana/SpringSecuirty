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

    @Convert(converter = PermissionsConverter.class)
    private List<ApplicationUserPermission> permissions;

    private boolean accountNonExpired,accountNonLocked,credentialsNonExpired,enabled;
}

@Converter
class RolesConverter implements AttributeConverter<List<ApplicationUserRole>,String>{

    @Override
    public String convertToDatabaseColumn(List<ApplicationUserRole> roles) {
        if(roles.size()==0) return "";
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<ApplicationUserRole> convertToEntityAttribute(String rolesEncoded) {
        if(rolesEncoded.isEmpty()) return new ArrayList<>();
        return Arrays.stream(rolesEncoded.split(","))
                .map(ApplicationUserRole::valueOf).collect(Collectors.toList());
    }
}

@Converter
class PermissionsConverter implements AttributeConverter<List<ApplicationUserPermission>,String>{

    @Override
    public String convertToDatabaseColumn(List<ApplicationUserPermission> permissions) {
        if(permissions.size()==0) return "";
        return permissions.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<ApplicationUserPermission> convertToEntityAttribute(String permissionsEncoded) {
        if(permissionsEncoded.isEmpty()) return new ArrayList<>();
        return Arrays.stream(permissionsEncoded.split(","))
                .map(ApplicationUserPermission::valueOf).collect(Collectors.toList());
    }
}