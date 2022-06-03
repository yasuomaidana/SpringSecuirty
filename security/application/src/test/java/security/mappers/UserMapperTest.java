package security.mappers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.dtos.users.CreateUserDTO;
import security.models.users.User;
import security.config.security.ApplicationUserPermission;
import security.config.security.ApplicationUserRole;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static security.config.security.ApplicationUserPermission.STUDENT_READ;
import static security.config.security.ApplicationUserPermission.STUDENT_WRITE;
import static security.config.security.ApplicationUserRole.ADMIN;
import static security.config.security.ApplicationUserRole.ADMIN_TRAINEE;


class UserMapperTest {

    private CreateUserDTO createUserDto;

    @InjectMocks
    private UserMapper mapper;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    public void Setup(){

        mapper = new UserMapperImpl();
        createUserDto = CreateUserDTO.builder()
                .roles("0,1")
                .permissions("0,1")
                .username("username")
                .password("password")
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserDtoToUser() {
        when(mockPasswordEncoder.encode(createUserDto.getPassword())).thenReturn("random password");
        User user = mapper.createUserDtoToUser(createUserDto);
        assertNotSame("Password is different","password",user.getPassword());
        assertEquals("Usernames match","username",user.getUsername());

        List<ApplicationUserRole> expectedRoles = new ArrayList<>(asList(ADMIN,ADMIN_TRAINEE));
        assertEquals("Roles match",expectedRoles,user.getRoles());

        List<ApplicationUserPermission> expectedPermissions = new ArrayList<>(asList(STUDENT_READ, STUDENT_WRITE));
        assertEquals("Permissions match",expectedPermissions,user.getPermissions());

    }

    @Test
    void convertToListRole() {
        List<ApplicationUserRole> roles = mapper.convertToListRole(createUserDto);
        List<ApplicationUserRole> expectedRoles = new ArrayList<>(asList(ADMIN,ADMIN_TRAINEE));
        assertEquals("Roles match",expectedRoles,roles);
    }

    @Test
    void convertToListPermissions() {
        List<ApplicationUserPermission> permissions = mapper.convertToListPermissions(createUserDto);
        List<ApplicationUserPermission> expectedPermissions = new ArrayList<>(asList(STUDENT_READ, STUDENT_WRITE));
        assertEquals("Permissions match",expectedPermissions,permissions);
    }
}