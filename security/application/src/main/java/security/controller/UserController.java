package security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import security.dtos.users.CreateUserDTO;
import security.dtos.users.RoleToUserForm;
import security.dtos.users.UserShowAuthorities;
import security.jwt.JwtConfig;
import security.mappers.UserMapper;
import security.models.users.Role;
import security.models.users.User;
import security.services.user.UserDaoService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@PermitAll @Slf4j
public class UserController {
    private final UserDaoService userDaoService;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

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
        try {
            return ResponseEntity.created(uri).body(userDaoService.saveRole(role));
        }catch (RuntimeException re){
            return ResponseEntity.created(uri).body(role);
        }
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm roleToUserForm){
        userDaoService.addRoleToUser(roleToUserForm.getUsername(), roleToUserForm.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        String refreshToken = request.getHeader(jwtConfig.getRefreshTokenPrefix());
        if(!Strings.isNullOrEmpty(authorizationHeader) && !Strings.isNullOrEmpty(refreshToken)
                && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())){

            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(),"");
            try{
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .build().parseClaimsJws(token);

                Jws<Claims> claimsRefreshToken = Jwts.parserBuilder()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .build().parseClaimsJws(refreshToken);

                Claims body = claimsJws.getBody();

                String username = body.getSubject();

                if(!username.equals(claimsRefreshToken.getBody().getSubject())){
                    throw new RuntimeException("Usernames doesn't match");
                }

                User user = userDaoService.getUser(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

                @SuppressWarnings("unchecked")

                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = (Set<SimpleGrantedAuthority>) user.getAuthorities();

                String access_token =  Jwts.builder()
                        .setSubject(username)
                        .claim(jwtConfig.getAuthoritiesPrefix(),simpleGrantedAuthorities)
                        .setIssuer(request.getRequestURL().toString())
                        .setIssuedAt(new Date())
                        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(jwtConfig.getTokenExpirationAfterDays())))
                        .signWith(jwtConfig.getSecretKey()).compact();

                refreshToken = Jwts.builder()
                        .setSubject(username)
                        .setIssuer(request.getRequestURL().toString())
                        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(jwtConfig.getTokenExpirationAfterDays()* 2L)))
                        .signWith(jwtConfig.getSecretKey()).compact();

                response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix()+token);
                response.addHeader(jwtConfig.getRefreshTokenPrefix(),refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                Map<String,String> tokens = new HashMap<>();
                tokens.put(jwtConfig.getAuthorizationHeader(),token);
                tokens.put(jwtConfig.getRefreshTokenPrefix(),refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);


            } catch (Exception e){
                log.error(e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
