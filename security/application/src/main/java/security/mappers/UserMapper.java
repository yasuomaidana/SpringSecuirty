package security.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.auth.UserDetailsImplementation;
import security.dtos.users.CreateUserDTO;
import security.models.users.User;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mapping(target="password", expression = "java(encodePassword(userDto))")
    public abstract User createUserDtoToUser(CreateUserDTO userDto);

    @Mapping(target = "authorities", expression = "java(getAuthoritiesFromUser(user))")
    public abstract UserDetailsImplementation userToApplicationUser(User user, boolean accountNonExpired,
                                                                    boolean accountNonLocked,
                                                                    boolean credentialsNonExpired,
                                                                    boolean enabled);

    public String encodePassword(CreateUserDTO userDto){
        return passwordEncoder.encode(userDto.getPassword());
    }

    public Set<? extends GrantedAuthority> getAuthoritiesFromUser(User user){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if(user.getRoles()!=null) {
            user.getRoles().forEach(role -> authorities.addAll(role.getGrantedAuthorities()));
        }
        if(user.getPermissions()!=null) {
            user.getPermissions()
                    .forEach(permission ->
                            authorities.add(new SimpleGrantedAuthority(permission.getPermission())));
        }
        return authorities;
    }
}
