package security.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.auth.ApplicationUser;
import security.dtos.users.CreateUserDTO;
import security.models.users.PermissionsConverter;
import security.models.users.RolesConverter;
import security.models.users.User;
import security.config.security.ApplicationUserPermission;
import security.config.security.ApplicationUserRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    RolesConverter rolesConverter = new RolesConverter();
    PermissionsConverter permissionsConverter = new PermissionsConverter();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mapping(target="roles", expression = "java(convertToListRole(userDto))")
    @Mapping(target="permissions", expression = "java(convertToListPermissions(userDto))")
    @Mapping(target="password", expression = "java(encodePassword(userDto))")
    public abstract User createUserDtoToUser(CreateUserDTO userDto);

    @Mapping(target = "authorities", expression = "java(getAuthoritiesFromUser(user))")
    public abstract ApplicationUser userToApplicationUser(User user);

    public Set<ApplicationUserRole> convertToListRole(CreateUserDTO userDto){
        return rolesConverter.convertToEntityAttribute(userDto.getRoles());
    }

    public Set<ApplicationUserPermission> convertToListPermissions(CreateUserDTO userDto){
        return permissionsConverter.convertToEntityAttribute(userDto.getPermissions());
    }

    public String encodePassword(CreateUserDTO userDto){
        return passwordEncoder.encode(userDto.getPassword());
    }

    public Set<? extends GrantedAuthority> getAuthoritiesFromUser(User user){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if(user.getRoles()!=null)
        user.getRoles().forEach(role->authorities.addAll(role.getGrantedAuthorities()));
        if(user.getPermissions()!=null)
        user.getPermissions()
                .forEach(permission->
                        authorities.add(new SimpleGrantedAuthority(permission.getPermission())));
        return authorities;
    }
}
