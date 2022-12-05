package com.web.spring_boot_securitymaster.config;


import com.web.spring_boot_securitymaster.config.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http    // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests().antMatchers("/reg").not().fullyAuthenticated().antMatchers("/").permitAll()
                // защищенные URL
                .antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasAnyRole("ADMIN", "USER").anyRequest().authenticated().and().formLogin().loginPage("/login")  // Spring сам подставит свою логин форму
                .successHandler(new LoginSuccessHandler());
        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
