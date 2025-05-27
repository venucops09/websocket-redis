package com.websocket.redis.messaging.server.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security configuration class for setting up Spring Security.
 * Configures in-memory authentication for users, form-based login,
 * and allows access to WebSocket endpoints and login page without authentication.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/chat.html", true) // Redirects here after successful login
                .permitAll()
                .and()
                .logout().permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("userA").password("{noop}password").roles("USER")
                .and()
                .withUser("userB").password("{noop}password").roles("USER")
                .and()
                .withUser("userC").password("{noop}password").roles("USER");
    }
}

