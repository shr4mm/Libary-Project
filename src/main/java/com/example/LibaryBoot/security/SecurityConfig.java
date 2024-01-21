package com.example.LibaryBoot.security;

import com.example.LibaryBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final PeopleService peopleService;
    @Autowired
    public SecurityConfig(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .addFilterAfter(new HiddenHttpMethodFilter(), CsrfFilter.class)
                .authorizeRequests(authorize -> {
                    authorize
                            .requestMatchers("/people", "/books/new", "/books/{id}/edit", "books/show_orders").hasRole("ADMIN")
                            .requestMatchers("/auth/login", "/error", "/auth/registration")
                            .permitAll()
                            .anyRequest()
                            .hasAnyRole("USER", "ADMIN");
                })
                .formLogin(formLogin -> {
                    formLogin
                            .loginPage("/auth/login")
                            .loginProcessingUrl("/process_login")
//                           .defaultSuccessUrl("/books")
                            .failureUrl("/auth/login?error");
                })
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/auth/login"));
        return http.build();
    }


    ///Настройка аутентификации
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(peopleService)
                .passwordEncoder(getPasswordEncoder());
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
       return new BCryptPasswordEncoder();
    }
}
