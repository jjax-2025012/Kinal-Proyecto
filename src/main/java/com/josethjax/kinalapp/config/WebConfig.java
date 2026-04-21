package com.josethjax.kinalapp.config;

import com.josethjax.kinalapp.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/clientes/**", "/productos/**", "/ventas/**", "/usuarios/**", "/detalles-venta/**")
                .excludePathPatterns("/login", "/", "/api/auth/**", "/css/**", "/js/**", "/img/**");
    }
}