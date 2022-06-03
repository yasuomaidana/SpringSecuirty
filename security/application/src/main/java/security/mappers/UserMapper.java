package security.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.dtos.users.CreateUserDTO;
import security.models.users.PermissionsConverter;
import security.models.users.RolesConverter;
import security.models.users.User;
import security.security.ApplicationUserPermission;
import security.security.ApplicationUserRole;

import java.util.List;

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

    public List<ApplicationUserRole> convertToListRole(CreateUserDTO userDto){
        return rolesConverter.convertToEntityAttribute(userDto.getRoles());
    }

    public List<ApplicationUserPermission> convertToListPermissions(CreateUserDTO userDto){
        return permissionsConverter.convertToEntityAttribute(userDto.getPermissions());
    }

    public String encodePassword(CreateUserDTO userDto){
        return passwordEncoder.encode(userDto.getPassword());
    }
}
