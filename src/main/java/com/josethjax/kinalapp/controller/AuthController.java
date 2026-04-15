package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IUsuarioService;
import com.josethjax.kinalapp.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        log.info("=== INTENTO DE LOGIN ===");
        log.info("Request recibida: {}", request);

        try {
            String username = request.get("username");
            String password = request.get("password");

            log.info("Username: {}", username);
            log.info("Password recibida: {}", password != null ? "****" : "null");

            if (username == null || username.trim().isEmpty()) {
                log.error("Username vacio");
                Map<String, String> error = new HashMap<>();
                error.put("error", "El usuario es obligatorio");
                return ResponseEntity.status(401).body(error);
            }

            if (password == null || password.trim().isEmpty()) {
                log.error("Password vacio");
                Map<String, String> error = new HashMap<>();
                error.put("error", "La contrasena es obligatoria");
                return ResponseEntity.status(401).body(error);
            }

            log.info("Buscando usuario en base de datos...");
            Optional<Usuario> usuarioOpt = usuarioService.listarUsuarios().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();

            if (usuarioOpt.isEmpty()) {
                log.error("Usuario no encontrado: {}", username);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuario no encontrado");
                return ResponseEntity.status(401).body(error);
            }

            Usuario usuario = usuarioOpt.get();
            log.info("Usuario encontrado: {}", usuario.getUsername());
            log.info("Password en BD: {}", usuario.getPassword());
            log.info("Password recibida: {}", password);
            log.info("Comparacion: {}", usuario.getPassword().equals(password));

            if (!usuario.getPassword().equals(password)) {
                log.error("Contrasena incorrecta para usuario: {}", username);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Contrasena incorrecta");
                return ResponseEntity.status(401).body(error);
            }

            if (usuario.getEstado() != 1) {
                log.error("Usuario inactivo: {}", username);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuario inactivo. Contacte al administrador.");
                return ResponseEntity.status(401).body(error);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login exitoso");
            response.put("username", usuario.getUsername());
            response.put("rol", usuario.getRol());
            response.put("codigoUsuario", usuario.getCodigoUsuario());

            log.info("Login exitoso para: {}", username);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error en login: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}