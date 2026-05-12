package com.josethjax.kinalapp.config;

import com.josethjax.kinalapp.Service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // Recursos públicos
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/api/auth/**",
                                "/api/usuarios"         // registro desde login
                        ).permitAll()

                        // Solo ADMIN: crear, editar, eliminar, gestión usuarios
                        .requestMatchers(
                                "/clientes/nuevo",
                                "/clientes/guardar",
                                "/clientes/editar/**",
                                "/clientes/eliminar/**",
                                "/productos/nuevo",
                                "/productos/guardar",
                                "/productos/editar/**",
                                "/productos/eliminar/**",
                                "/ventas/nuevo",
                                "/ventas/guardar",
                                "/ventas/anular/**",
                                "/detalles-venta/**",
                                "/usuarios/**",
                                "/api/clientes/**",
                                "/api/productos/**",
                                "/api/ventas/**",
                                "/api/usuarios/**"
                        ).hasRole("ADMIN")

                        // USER y ADMIN: solo ver listados y detalles
                        .requestMatchers(
                                "/index",
                                "/clientes",
                                "/productos",
                                "/ventas",
                                "/ventas/detalle/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true")
                )
                // Manejo de acceso denegado → página de error 403 o JSON
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.setContentType("application/json;charset=UTF-8");
                                response.getWriter().write("{\"error\":\"Solo los administradores pueden realizar esta acción\"}");
                            } else {
                                response.sendRedirect("/403");
                            }
                        })
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}