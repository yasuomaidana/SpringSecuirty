package security.config.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import security.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static security.config.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf()
                .ignoringAntMatchers("/login*","/logout*")// to ignore specific paths from csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeHttpRequests()
                //WhiteListing urls
                .antMatchers("/","/index.html","/css/*","/js/*","/login*","/logout").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginPage("/login")
                    .defaultSuccessUrl("/courses",true)
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("secret_key")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new
                            AntPathRequestMatcher("/logout", "POST"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("XSRF-TOKEN","remember-me","JSESSIONID")
                    .logoutSuccessUrl("/login");
    }

    //How do you retrieve users from your database, this version is hardcoded
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annSmith = User
                .builder()
                .username("anna")
                .password(passwordEncoder.encode("password"))
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails lindaUser =User
                .builder()
                .username("linda")
                .password(passwordEncoder.encode("pass"))
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser =User
                .builder()
                .username("tom")
                .password(passwordEncoder.encode("pass"))
                .authorities(ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities())
                .build();

        if (!userRepository.findByUsername("linda").isPresent()){
            security.models.users.User user = security.models.users.User.builder()
                    .username("linda")
                    .password(lindaUser.getPassword())
                    .roles(Arrays.asList(ApplicationUserRole.ADMIN, ApplicationUserRole.ADMIN_TRAINEE))
                    .permissions(new ArrayList<>(Arrays.asList(new ApplicationUserPermission[]{ApplicationUserPermission.STUDENT_READ, ApplicationUserPermission.STUDENT_WRITE})))
                    .build();
            userRepository.save(user);
        }

        if (!userRepository.findByUsername("tom").isPresent()){
            security.models.users.User user = security.models.users.User.builder()
                    .username("tom")
                    .password(tomUser.getPassword())
                    .roles(Collections.singletonList(ApplicationUserRole.ADMIN))
                    .permissions(new ArrayList<>(Arrays.asList(new ApplicationUserPermission[]{ApplicationUserPermission.STUDENT_READ, ApplicationUserPermission.STUDENT_WRITE})))
                    .build();
            userRepository.save(user);
        }
        return new InMemoryUserDetailsManager(annSmith,lindaUser,tomUser);
    }
}
