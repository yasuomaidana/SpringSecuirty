package security.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.mappers.UserMapper;
import security.models.users.User;
import security.services.user.UserDaoServiceImplementation;

@Service
@AllArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserDaoServiceImplementation userDAOServiceImplementation;
    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAOServiceImplementation.getUser(username).orElseThrow(()
                ->new UsernameNotFoundException(String.format("User %s not found",username)));

        return mapper.userToApplicationUser(user);
    }

}
