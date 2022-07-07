package security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import security.services.user.UserDaoService;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean //Executes whatever is inside after loading the program
	CommandLineRunner run(UserDaoService service){
		return args -> {
			System.out.println(service.getUsers());
		};
	}
	/*
	* */

}
