package security.services.user;

import security.models.users.Permission;
import security.models.users.Role;
import security.models.users.User;

import java.util.List;
import java.util.Optional;

public interface UserDaoService {
    User saveUser(User user);
    Role saveRole(Role role);
    Permission savePermission(Permission permission);
    void addRoleToUser(String username, String roleName);
    void addPermissionToUser(String username, String permissionName);
    Optional<User> getUser(String name);
    List<User> getUsers();
}
