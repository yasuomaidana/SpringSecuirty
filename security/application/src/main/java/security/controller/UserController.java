package security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.dtos.users.CreateUserDTO;
import security.dtos.users.UserShowAuthorities;
import security.mappers.UserMapper;
import security.models.users.User;
import security.services.user.UserDaoService;

import javax.annotation.security.PermitAll;
import java.util.HashSet;
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
                .stream().map(user->
                        userMapper.UserToShowUser(user))
                .collect(Collectors.toList()));
    }

    @PostMapping("/users/save")
    public ResponseEntity<UserShowAuthorities> saveUser(@RequestBody CreateUserDTO newUser){
        User createdUser =  userDaoService.saveUser(userMapper.createUserDtoToUser(newUser));

        return ResponseEntity.ok().body(userMapper.UserToShowUser(createdUser));
    }


}
