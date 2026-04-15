package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IUsuarioService;
import com.josethjax.kinalapp.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Optional<Usuario> usuarioOpt = usuarioService.listarUsuarios().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
        }

        if (usuario.getEstado() != 1) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario inactivo"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login exitoso");
        response.put("username", usuario.getUsername());
        response.put("rol", usuario.getRol());
        response.put("codigoUsuario", usuario.getCodigoUsuario());

        return ResponseEntity.ok(response);
    }
}