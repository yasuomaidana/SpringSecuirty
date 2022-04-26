package security.security;

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

import static org.springframework.http.HttpMethod.*;
import static security.security.ApplicationUserPermission.COURSE_WRITE;
import static security.security.ApplicationUserPermission.STUDENT_WRITE;
import static security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                //WhiteListing urls
                .antMatchers("/","index","/css/*","/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .antMatchers(DELETE,"/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission(), STUDENT_WRITE.getPermission())
                .antMatchers(POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission()) // covered using annotation
                .antMatchers(PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                //.antMatchers(GET,"/management/api/**").hasAnyRole(ADMIN.name(),ADMIN_TRAINEE.name()) done using annotation
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

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
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser =User
                .builder()
                .username("tom")
                .password(passwordEncoder.encode("pass"))
                .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(annSmith,lindaUser,tomUser);
    }
}
