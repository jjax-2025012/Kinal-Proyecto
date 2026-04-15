package com.josethjax.kinalapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Verificar si hay usuario en sesión
        Object usuario = request.getSession().getAttribute("usuario");

        if (usuario == null) {
            // Redirigir al login
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}