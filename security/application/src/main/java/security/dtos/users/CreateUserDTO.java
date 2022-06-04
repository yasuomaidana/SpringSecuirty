package security.dtos.users;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CreateUserDTO {
    private String username;
    private String password;
    private String roles;
    private String permissions;
}
