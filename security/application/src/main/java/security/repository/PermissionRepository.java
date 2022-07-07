package security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.models.users.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
    Optional<Permission> findByName(String name);
}
