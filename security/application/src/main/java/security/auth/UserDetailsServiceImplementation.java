package security.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.mappers.UserMapper;
import security.models.users.User;
import security.services.UserDAOService;

@Service
@AllArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserDAOService userDAOService;
    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAOService.getUserByUsername(username).orElseThrow(()
                ->new UsernameNotFoundException(String.format("User %s not found",username)));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        return mapper.userToApplicationUser(user);
    }

}
