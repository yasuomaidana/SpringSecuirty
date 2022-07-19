package security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor @Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {
    private JwtConfig jwtConfig;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull
                                    HttpServletResponse response,
                                    @NonNull
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if(!request.getServletPath().startsWith("/api/login") &&
                !Strings.isNullOrEmpty(authorizationHeader) &&
                authorizationHeader.startsWith(jwtConfig.getTokenPrefix())){
            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(),"");
            try{

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .build().parseClaimsJws(token);

                Claims body = claimsJws.getBody();

                String username = body.getSubject();

                @SuppressWarnings("unchecked")
                List<Map<String, String>> authorities =
                        (List<Map<String, String>>) body.get(jwtConfig.getAuthoritiesPrefix());

                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities == null ? new HashSet<>():authorities.stream()
                        .map(
                                m -> new SimpleGrantedAuthority(m.get(jwtConfig.getSingleAuthorityPrefix())))
                        .collect(Collectors.toSet());

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(username,null,simpleGrantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                log.error("Token {} cannot be trusted, token Expired", token);
                throw new RuntimeException(String.format("Token %s cannot be trusted, token Expired", token));
            } catch (UnsupportedJwtException e) {
                log.error("Token {} cannot be trusted, token unsupported", token);
                throw new RuntimeException(String.format("Token %s cannot be trusted, token unsupported", token));
            } catch (MalformedJwtException e) {
                log.error("Token {} cannot be trusted. token malformed", token);
                response.addHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
                throw new RuntimeException(String.format("Token %s cannot be trusted. token malformed", token));
            } catch (SignatureException e) {
                log.error("Token {} cannot be trusted, signature exception", token);
                throw new RuntimeException(String.format("Token %s cannot be trusted, signature exception", token));
            } catch (IllegalArgumentException e) {
                log.error("Token {} cannot be trusted", token);
                throw new RuntimeException(String.format("Token %s cannot be trusted", token));
            } catch (AuthenticationException ae){
                log.error("Token {} cannot be authenticated", token);
                response.addHeader("error",ae.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",ae.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }

        filterChain.doFilter(request,response);
    }
}
