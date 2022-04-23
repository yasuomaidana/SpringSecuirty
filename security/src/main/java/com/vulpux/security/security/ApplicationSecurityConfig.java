package com.vulpux.security.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.vulpux.security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                //WhiteListing urls
                .antMatchers("/","index","/css/*","/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
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
                .roles(STUDENT.name())
                .build();

        UserDetails lindaUser =User
                .builder()
                .username("linda")
                .password(passwordEncoder.encode("pass"))
                .roles(ADMIN.name())
                .build();

        UserDetails tomUser =User
                .builder()
                .username("tom")
                .password(passwordEncoder.encode("pass"))
                .roles(ADMIN_TRAINEE.name())
                .build();

        return new InMemoryUserDetailsManager(annSmith,lindaUser,tomUser);
    }
}
