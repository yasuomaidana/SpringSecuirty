package security.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import security.jwt.JwtConfig;
import security.jwt.JwtTokenVerifier;
import security.jwt.JwtUsernameAndPasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private JwtConfig jwtConfig;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),jwtConfig);
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.cors().and()
                .csrf()//.disable()
                    .ignoringAntMatchers("/api/login/**","/logout*","/api/users/**","/api/role/**")// to ignore specific paths from csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(STATELESS)
                .and()
                .addFilter(usernamePasswordAuthenticationFilter)
                .addFilterBefore(new JwtTokenVerifier(jwtConfig),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                //WhiteListing urls
                .antMatchers("/","/index.html","/css/*","/js/*","/api/login/**","/logout","/api/users/*","/api/role/**").permitAll()
                .anyRequest()
                .authenticated();
    }

}
