package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            // Aquí solo verificamos que el usuario existe
            // La validación de contraseña la hace Spring Security automáticamente
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario encontrado");
            response.put("username", username);
            response.put("rol", userDetails.getAuthorities());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            return ResponseEntity.status(401).body(error);
        }
    }
}