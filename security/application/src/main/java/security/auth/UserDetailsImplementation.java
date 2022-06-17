package security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Builder @Getter @AllArgsConstructor
public class UserDetailsImplementation implements UserDetails {

    private final Set<? extends GrantedAuthority> authorities;
    private final String password;
    private final String username;
    private final boolean accountNonExpired,accountNonLocked,credentialsNonExpired,enabled;

}
