package security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import security.dtos.users.CreateUserDTO;
import security.mappers.UserMapper;
import security.models.users.User;
import security.services.user.UserDaoServiceImplementation;

import static security.config.security.ApplicationUserPermission.STUDENT_READ;

@Component @RequiredArgsConstructor
public class UserLoader implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private final UserDaoServiceImplementation userDAOServiceImplementation;
    @Autowired
    private final UserMapper userMapper;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        CreateUserDTO newUser;
        User userMapped;
        if(!userDAOServiceImplementation.getUser("tom").isPresent()){
            newUser =
                    CreateUserDTO.builder().username("tom").password("pass").build();
            userMapped = userMapper.createUserDtoToUser(newUser);
            userDAOServiceImplementation.saveUser(userMapped);
        }
        if(!userDAOServiceImplementation.getUser("john").isPresent()){
            newUser =
                    CreateUserDTO.builder().username("john").password("pass").build();
            userMapped = userMapper.createUserDtoToUser(newUser);
            userDAOServiceImplementation.saveUser(userMapped);
            userDAOServiceImplementation.addPermissionToUser("john", STUDENT_READ.name());
        }
        if (!userDAOServiceImplementation.getUser("linda").isPresent()){
            newUser =
                    CreateUserDTO.builder().username("linda")
                            .password("pass")
                            .build();
            userMapped = userMapper.createUserDtoToUser(newUser);
            userDAOServiceImplementation.saveUser(userMapped);
        }
        if (!userDAOServiceImplementation.getUser("anna").isPresent()){
            newUser =
                    CreateUserDTO.builder().username("anna")
                            .password("pass")
                            .build();
            userMapped = userMapper.createUserDtoToUser(newUser);
            userDAOServiceImplementation.saveUser(userMapped);
        }
    }
}
