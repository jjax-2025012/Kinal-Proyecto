package com.josethjax.kinalapp.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Verificar si es una petición de recursos estáticos o login
        String uri = request.getRequestURI();
        if (uri.equals("/login") || uri.equals("/") || uri.startsWith("/css/") ||
                uri.startsWith("/js/") || uri.startsWith("/img/") || uri.startsWith("/api/auth/")) {
            return true;
        }

        // Verificar sessionStorage via cookie
        Cookie[] cookies = request.getCookies();
        boolean hasUserCookie = false;

        if (cookies != null) {
            hasUserCookie = Arrays.stream(cookies)
                    .anyMatch(cookie -> "usuario".equals(cookie.getName()));
        }

        if (!hasUserCookie) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}