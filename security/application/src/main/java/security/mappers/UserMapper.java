package security.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.auth.UserDetailsImplementation;
import security.dtos.users.CreateUserDTO;
import security.dtos.users.UserShowAuthorities;
import java.util.HashSet;
import security.models.users.User;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mapping(target="password", expression = "java(encodePassword(userDto))")
    public abstract User createUserDtoToUser(CreateUserDTO userDto);

    @Mapping(target = "authorities", expression = "java(user.getAuthorities())")
    public abstract UserDetailsImplementation userToApplicationUser(User user, boolean accountNonExpired,
                                                                    boolean accountNonLocked,
                                                                    boolean credentialsNonExpired,
                                                                    boolean enabled);

    @Mapping(target = "permissions", expression = "java(getAuthoritiesFromUser(user))")
    public abstract UserShowAuthorities UserToShowUser(User user);
    public String encodePassword(CreateUserDTO userDto){
        return passwordEncoder.encode(userDto.getPassword());
    }

    public HashSet<GrantedAuthority> getAuthoritiesFromUser(User user){
        return new HashSet<>(user.getAuthorities());
    }
}
