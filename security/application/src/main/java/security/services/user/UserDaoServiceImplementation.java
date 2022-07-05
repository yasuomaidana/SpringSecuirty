package security.services.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import security.config.security.ApplicationUserPermission;
import security.config.security.ApplicationUserRole;
import security.models.users.Permission;
import security.models.users.Role;
import security.models.users.User;
import security.repository.PermissionRepository;
import security.repository.RoleRepository;
import security.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserDaoServiceImplementation implements UserDaoService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Optional<User> getUser(String username){
        log.info("Fetching {} user",username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(User user){
        log.info("Saving {} user",user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {

        try{
            ApplicationUserRole.valueOf(role.getName());
            return roleRepository.saveAndFlush(role);
        } catch (DataIntegrityViolationException cve){
            log.info(String.format("Trying to save duplicated role %s",role.getName()));
            Throwable cause = cve.getCause();
            log.info(role.getName()+" is already stored");
            log.info(cause.toString());
            throw new RuntimeException("It is needed a custom error exception");
        } catch (IllegalArgumentException iae){
            log.error(String.format("Trying to save non-existing role %s",role.getName()));
            throw iae;
        }finally {
            log.info("Saving {} role",role.getName());
        }
    }

    public Role saveRole(String role) {
        Role newRole = new Role();
        newRole.setName(role);
        return saveRole(newRole);
    }

    @Override
    public Permission savePermission(Permission permission) {
        try{
            ApplicationUserPermission.valueOf(permission.getName());
            return permissionRepository.save(permission);
        } catch (ConstraintViolationException cve){
            log.info(String.format("Trying to save duplicated permission %s",permission.getName()));
            Throwable cause = cve.getCause();
            String constraintName = cve.getConstraintName();
            log.info(permission.getName()+" is already stored");
            log.info(constraintName);
            log.info(cause.toString());
        } catch (IllegalArgumentException iae){
            log.error(String.format("Trying to save non-existing permission %s",permission.getName()));
            throw iae;
        }finally {
            log.info("Saving {} permission",permission.getName());
        }
        return permission;
    }

    public Permission savePermission(String permission) {
        Permission newPermission = new Permission();
        newPermission.setName(permission);
        return savePermission(newPermission);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding {} role to {} user",roleName,username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException(String.format("User %s doesn't exists",username)));

        Role role = roleRepository.findByName(roleName)
                        .orElse(saveRole(roleName));

        user.getRolesRaw().add(role);

    }

    @Override
    public void addPermissionToUser(String username, String permissionName) {
        log.info("Adding {} permission to {} user",permissionName,username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException(String.format("User %s doesn't exists",username)));
        Permission permission = permissionRepository.findByName(permissionName)
                .orElse(savePermission(permissionName));
        user.getPermissionsRaw().add(permission);
    }

    @Override
    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    public List<User> getUsersByPage(int page){
        Pageable usersPage = PageRequest.of(page,5);
        return userRepository.findAll(usersPage).getContent();
    }

    public List<User> getUserSortedByPage(int page){
        Pageable usersPage = PageRequest.of(page,5, Sort.by("username"));
        return userRepository.findAll(usersPage).getContent();
    }
}
