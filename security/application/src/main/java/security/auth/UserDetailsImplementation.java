package security.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.security.ApplicationUserPermission;
import security.security.ApplicationUserRole;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class ApplicationUserServiceImplementation implements UserDetailsService {

    private final UserRepository applicationUserDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = applicationUserDAO.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(String.format("User %s not found",username)));

        return ApplicationUser.builder()
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .authorities(getAuthorities(user))
                .build();
    }

    private Set<? extends GrantedAuthority> getAuthorities(User user){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for(ApplicationUserRole role: user.getRoles()){
            authorities.addAll(role.getGrantedAuthorities());
        }
        for(ApplicationUserPermission permission: user.getPermissions()){
            authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
        }
        return authorities;
    }
}
