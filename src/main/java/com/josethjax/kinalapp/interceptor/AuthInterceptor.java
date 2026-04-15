package com.josethjax.kinalapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Obtener cookies de la petición
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();

        boolean hasUserCookie = false;

        if (cookies != null) {
            hasUserCookie = Arrays.stream(cookies)
                    .anyMatch(cookie -> "usuario".equals(cookie.getName()));
        }

        // También verificar atributo de sesión (por si acaso)
        Object usuarioSesion = request.getSession().getAttribute("usuario");

        if (!hasUserCookie && usuarioSesion == null) {
            // Redirigir al login si no hay sesión
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}