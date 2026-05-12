package com.josethjax.kinalapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Spring Security maneja la autenticación
    // No se necesita el AuthInterceptor manual
}