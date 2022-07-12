package security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import security.services.user.UserDaoService;

import static security.config.security.ApplicationUserRole.*;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean //Executes whatever is inside after loading the program
	CommandLineRunner run(UserDaoService service){
		return args -> {
			givePermission(service,"linda",ADMIN.name());
			givePermission(service,"tom",ADMIN_TRAINEE.name());
			givePermission(service,"anna", STUDENT.name());
		};
	}

	public static void givePermission(UserDaoService service,String username,String role){
		if (service.getUser(username).isPresent()){
			service.addRoleToUser(username, role);
		}
	}

}
