package main.config;

import javax.sql.DataSource;
import main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  UsersService userService;

  @Autowired
  private DataSource dataSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf()
           .disable()
           .authorizeRequests()
           //Доступ только для не зарегистрированных пользователей
           .antMatchers("/api/auth/register").not().fullyAuthenticated()
           //Доступ только для пользователей с ролью Модератор
           .antMatchers("/somesecretpage/**").hasRole("MODERATOR")
           //Доступ разрешен всем пользователям
            .antMatchers( //тут пока не понял
                "/", "/css/**", "/js/**", "/search/**", "/api/post/**", "/api/tag/**"
                , "/api/calendar/**", "/api/settings", "/api/init").permitAll()
           //Все остальные страницы требуют аутентификации
        .anyRequest().authenticated()
        .and()
            //Настройка для входа в систему
            .formLogin()
            .loginPage("/login")
           //Перенарпавление на главную страницу после успешного входа
           .defaultSuccessUrl("/")
           .permitAll()
        .and()
           //запомнили сессию
           .rememberMe()
        .and()
           .logout()
           .permitAll()
           .logoutSuccessUrl("/");
  }

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().dataSource(dataSource)
        .passwordEncoder(NoOpPasswordEncoder.getInstance())
        .usersByUsernameQuery("select name, password from users where email = ")
        .authoritiesByUsernameQuery("select name, is_moderator from user where name = ");
        //userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
  }
  }
