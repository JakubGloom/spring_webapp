package net.atos.spring_webapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

//@EnableOAuth2Sso
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    DataSource dataSource;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("SELECT u.email, u.password, u.enable FROM user u WHERE u.email = ?")
                .authoritiesByUsernameQuery(
                        "SELECT u.email, p.role_name FROM " +
                                "user u " +
                                "join " +
                                "user_permission up on (u.user_id = up.user_id) " +
                                "join " +
                                "permission p on (p.permission_id = up.permission_id) " +
                                "WHERE u.email = ?")
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // żądania autoryzowane określoną rolą
                .antMatchers("/post&*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/post/delete").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/post&edit&*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/editpost").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // pozostałe nie są autoryzowane
                .anyRequest().permitAll()
                .and().csrf().disable()
                .formLogin().loginPage("/login")    // adres formularza logowania
                .usernameParameter("email")     // nazwa th:name 1 parametru z formularza
                .passwordParameter("password")  // nazwa th:name 2 parametru z formularza
                .loginProcessingUrl("/login_process")   // zadres na jaki zostaną te parametry przekazane metodą POST
                .failureUrl("/login_error")      // adres przekierowania po błędynym logowaniu
                .defaultSuccessUrl("/")                 // adres przekierowania po poprawnym logowaniu
                .and()
//                .oauth2Login()
//                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }



}