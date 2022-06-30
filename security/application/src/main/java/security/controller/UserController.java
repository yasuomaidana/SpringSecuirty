package security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.dtos.users.UserShowAuthorities;
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

    @GetMapping("/users")
    public ResponseEntity<List<UserShowAuthorities>> getUsers(){
        return ok().body(userDaoService.getUsers()
                .stream().map(user->
                        UserShowAuthorities.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .permissions(new HashSet<>(user.getAuthorities()))
                                .build())
                .collect(Collectors.toList()));
    }
}
