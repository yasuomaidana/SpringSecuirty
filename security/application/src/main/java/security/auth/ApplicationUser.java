package security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Builder @Getter @AllArgsConstructor
public class ApplicationUser implements UserDetails {

    private final List<? extends GrantedAuthority> authorities;
    private final String password;
    private final String username;
    private final boolean accountNonExpired,accountNonLocked,credentialsNonExpired,enabled;

}