package security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import security.config.security.ApplicationUserPermission;
import security.dtos.users.CreateUserDTO;
import security.mappers.UserMapper;
import security.models.users.User;
import security.services.user.UserDaoServiceImplementation;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static security.config.security.ApplicationUserPermission.STUDENT_READ;
import static security.config.security.ApplicationUserPermission.STUDENT_WRITE;
import static security.config.security.ApplicationUserRole.*;

@Component
public class UserLoader implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private UserDaoServiceImplementation userDAOServiceImplementation;
    @Autowired
    private UserMapper userMapper;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        CreateUserDTO newUser;
        String roles;
        String permissions;
        User userMapped;
        if(!userDAOServiceImplementation.getUser("tom").isPresent()){
            roles = String.valueOf(ADMIN_TRAINEE.ordinal());
            newUser =
                    CreateUserDTO.builder().username("tom").password("pass").build();
            userMapped = userMapper.createUserDtoToUser(newUser);
            userDAOServiceImplementation.saveUser(userMapped);
        }
        if (!userDAOServiceImplementation.getUser("linda").isPresent()){
            roles = String.valueOf(ADMIN.ordinal());
            permissions = Stream.of(
                    new ApplicationUserPermission[]{STUDENT_READ, STUDENT_WRITE})
                    .map(permission->String.valueOf(permission.ordinal())).collect(Collectors.joining(","));
            newUser =
                    CreateUserDTO.builder().username("linda")
                            .password("pass")
                            .build();
            userMapped = userMapper.createUserDtoToUser(newUser);
            userDAOServiceImplementation.saveUser(userMapped);
        }
        if (!userDAOServiceImplementation.getUser("anna").isPresent()){
            roles = String.valueOf(STUDENT.ordinal());
            newUser =
                    CreateUserDTO.builder().username("anna")
                            .password("pass")
                            .build();
            userMapped = userMapper.createUserDtoToUser(newUser);
            userDAOServiceImplementation.saveUser(userMapped);
        }
    }
}
