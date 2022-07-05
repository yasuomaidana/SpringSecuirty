package security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import security.dtos.users.CreateUserDTO;
import security.dtos.users.UserShowAuthorities;
import security.mappers.UserMapper;
import security.models.users.Role;
import security.models.users.User;
import security.services.user.UserDaoService;

import javax.annotation.security.PermitAll;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@PermitAll
public class UserController {
    private final UserDaoService userDaoService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserShowAuthorities>> getUsers(){
        return ok().body(userDaoService.getUsers()
                .stream().map(userMapper::UserToShowUser)
                .collect(Collectors.toList()));
    }

    @PostMapping("/users/save")
    public ResponseEntity<UserShowAuthorities> saveUser(@RequestBody CreateUserDTO newUser){
        User createdUser =  userDaoService.saveUser(userMapper.createUserDtoToUser(newUser));
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/users/save").toUriString());
        return ResponseEntity.created(uri).body(userMapper.UserToShowUser(createdUser));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userDaoService.saveRole(role));
    }

}
