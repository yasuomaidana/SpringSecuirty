package security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.dtos.users.UserAuthenticationDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor @Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserAuthenticationDTO authenticationRequest = null;
        try {

            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UserAuthenticationDTO.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword());

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AuthenticationException authenticationException){
            if(authenticationRequest!=null){
                log.warn("Trying to log into "+ authenticationRequest.getUsername());
            }
            assert authenticationRequest != null;
            authenticationException.addSuppressed(new Throwable(authenticationRequest.getUsername()));
            throw authenticationException;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim(jwtConfig.getAuthoritiesPrefix(),authResult.getAuthorities())
                .setIssuer("Who wrote the issue")
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(jwtConfig.getSecretKey()).compact();

        String refreshToken = Jwts.builder()
                .setSubject(authResult.getName())
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

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        if (Arrays.stream(failed.getSuppressed()).findFirst().isPresent()){
            log.warn("Processing "+Arrays.stream(failed.getSuppressed()).findFirst().get().getMessage());
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
