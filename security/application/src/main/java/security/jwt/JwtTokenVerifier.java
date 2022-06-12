package security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private JwtConfig jwtConfig;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if(!Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())){
            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(),"");
            try{

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .build().parseClaimsJws(token);

                Claims body = claimsJws.getBody();

                String username = body.getSubject();

                List<Map<String, String>> authorities =
                        (List<Map<String, String>>) body.get(jwtConfig.getAuthoritiesPrefix());

                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                        .map(
                                m -> new SimpleGrantedAuthority(m.get(jwtConfig.getSingleAuthorityPrefix())))
                        .collect(Collectors.toSet());

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(username,null,simpleGrantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                throw new RuntimeException(String.format("Token %s cannot be trusted, token Expired", token));
            } catch (UnsupportedJwtException e) {
                throw new RuntimeException(String.format("Token %s cannot be trusted, token unsupported", token));
            } catch (MalformedJwtException e) {
                throw new RuntimeException(String.format("Token %s cannot be trusted. token malformed", token));
            } catch (SignatureException e) {
                throw new RuntimeException(String.format("Token %s cannot be trusted, signature exception", token));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(String.format("Token %s cannot be trusted", token));
            }
        }

        filterChain.doFilter(request,response);
    }
}
