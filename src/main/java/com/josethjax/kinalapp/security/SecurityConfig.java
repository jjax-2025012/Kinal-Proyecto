package com.josethjax.kinalapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // SOLO estos endpoints son públicos
                        .requestMatchers("/usuarios").permitAll()  // POST crear usuario
                        .requestMatchers("/api/auth/**").permitAll()  // Login

                        // El resto de endpoints de usuarios SOLO para ADMIN
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // Otros endpoints protegidos
                        .requestMatchers("/clientes/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/productos/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/ventas/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/detalles-venta/**").hasAnyRole("ADMIN", "USER")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}