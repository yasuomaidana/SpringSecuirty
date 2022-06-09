package security.dtos.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class UserAuthenticationDTO {
    private String username;
    private String password;
}
