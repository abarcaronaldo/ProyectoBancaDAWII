package com.banco.accountservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/cuentas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/cuentas").hasRole("ADMIN")

                        .requestMatchers("/api/cuentas/{id}").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers("/api/cuentas/clienteId/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/cuentas/{id}/estado").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cuentas/{id}/saldo").hasRole("ADMIN")
                        .requestMatchers("/api/cuentas/numero/{numeroCuenta}").hasAnyRole("ADMIN", "CLIENTE")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
