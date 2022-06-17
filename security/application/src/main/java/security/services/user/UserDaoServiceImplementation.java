package security.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import security.models.users.User;
import security.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDAOService {
    private final UserRepository userRepository;

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void createUser(User user){
        userRepository.save(user);
    }
}
