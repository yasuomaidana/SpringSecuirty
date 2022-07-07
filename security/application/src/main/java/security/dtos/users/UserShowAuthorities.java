package security.dtos.users;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data @Builder
public class UserShowAuthorities {
    private Long id;
    private String username;
    private String password;
    private Set<GrantedAuthority> permissions;

}
