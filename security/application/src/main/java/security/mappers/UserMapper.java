package security.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.auth.UserDetailsImplementation;
import security.dtos.users.CreateUserDTO;
import security.dtos.users.UserShowAuthorities;
import security.models.users.User;

import java.util.HashSet;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mapping(target="password", expression = "java(encodePassword(userDto))")
    public abstract User createUserDtoToUser(CreateUserDTO userDto);

    @Mapping(target = "authorities", expression = "java(user.getAuthorities())")
    @Mapping(target = "accountNonExpired", expression = "java(true)")
    @Mapping(target = "credentialsNonExpired", expression = "java(true)")
    @Mapping(target = "accountNonLocked", expression = "java(true)")
    @Mapping(target = "enabled", expression = "java(true)")
    public abstract UserDetailsImplementation userToApplicationUser(User user);

    @Mapping(target = "permissions", expression = "java(getAuthoritiesFromUser(user))")
    public abstract UserShowAuthorities UserToShowUser(User user);
    public String encodePassword(CreateUserDTO userDto){
        return passwordEncoder.encode(userDto.getPassword());
    }

    public HashSet<GrantedAuthority> getAuthoritiesFromUser(User user){
        return new HashSet<>(user.getAuthorities());
    }
}
