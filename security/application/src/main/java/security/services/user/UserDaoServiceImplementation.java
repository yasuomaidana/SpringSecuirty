package security.services.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import security.config.security.ApplicationUserRole;
import security.models.users.Role;
import security.models.users.User;
import security.repository.RoleRepository;
import security.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserDaoServiceImplementation implements UserDaoService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Optional<User> getUser(String username){
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        try{
            ApplicationUserRole.valueOf(role.getName());
            return roleRepository.save(role);
        } catch (ConstraintViolationException cve){

            log.info(String.format("Trying to save duplicated role %s",role.getName()));
            Throwable cause = cve.getCause();
            String constraintName = cve.getConstraintName();
            log.info(role.getName()+" is already stored");
            log.info(constraintName);
            log.info(cause.toString());
        } catch (IllegalArgumentException iae){
            log.error(String.format("Trying to save non-existing role %s",role.getName()));
            throw iae;
        }
        return role;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException(String.format("User %s doesn't exists",username)));
        Role role = roleRepository.findByName(roleName);
        user.getRolesRaw().add(role);

    }

    @Override
    public List<User> getUsers() {
        Pageable first5Users = PageRequest.of(0,5);
        return userRepository.findAll(first5Users).getContent();
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
