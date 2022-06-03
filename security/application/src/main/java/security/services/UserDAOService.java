package security.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.models.users.User;
import security.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserDAOService {
    private final UserRepository userRepository;

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()
                ->new UsernameNotFoundException(String.format("User %s not found",username)));
    }

    public void createUser(){

    }
}
