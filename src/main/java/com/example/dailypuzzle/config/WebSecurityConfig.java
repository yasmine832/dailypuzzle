package com.example.dailypuzzle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.sql.DataSource;

import static javax.management.Query.and;

//this code is based on baeldung.com/spring-security-tymeleaf and howtodoinjava.com/spring-security/spring-security-tutorial

@Configuration // provide custom configur
@EnableWebSecurity

public class WebSecurityConfig { //spring boot will automatically scan this class and register (cause of bean)

//    @Bean // in memory storage
//    public UserDetailsService userDetailsService() {
//
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.builder()
//                .username("user")
//                .password("password")
//                .roles("USER").build());
//        return manager;
//
//    }


    // to store and retrieve user data from db, connects db directly tru jdbc
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/dailypuzzle")
                .username("root")
                //.password("")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();


    }

    //pw encoder bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //custom login page and define security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/login", "/register", "/hello")//voorlopig

                        .permitAll() //allow login and register publicly
                        .requestMatchers("/puzzles/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated() //restrict any other endpoint


                )
                .formLogin(formLogin ->
                        formLogin
                                //.loginPage("/login")//todo
                                .defaultSuccessUrl("/hello", true) //Todoo "/home"
                                .failureUrl("/login?error=true")



                )
                .logout(logout -> logout
                        .logoutUrl("/logout")//todo
                        .logoutSuccessUrl("/login?logout=true")
                );


        http.sessionManagement(sessionManagement ->
                sessionManagement
                        .maximumSessions(1)
                        .expiredUrl("/login?sessionExpired=true")
        );

        return http.build();
    }


    //configure userdetailsservice with jdbcuserdetailsmanager
    @Bean
    public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role AS authority FROM user WHERE username = ?");
        return userDetailsManager;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }




}








