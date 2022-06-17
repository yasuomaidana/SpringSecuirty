package security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.models.users.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
