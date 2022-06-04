package security.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static security.config.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
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

}
