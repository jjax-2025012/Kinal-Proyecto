package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IUsuarioService;
import com.josethjax.kinalapp.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController {

    private static final Logger log = LoggerFactory.getLogger(AuthViewController.class);

    private final IUsuarioService usuarioService;

    public AuthViewController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        log.info("=== MOSTRANDO FORMULARIO DE REGISTRO ===");
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario, Model model) {
        log.info("=== PROCESANDO REGISTRO DE USUARIO: {} ===", usuario.getUsername());

        try {
            // Validaciones
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                model.addAttribute("error", "El nombre de usuario es obligatorio");
                return "registro";
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                model.addAttribute("error", "La contraseña es obligatoria");
                return "registro";
            }
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "El email es obligatorio");
                return "registro";
            }

            // Verificar si ya existe el email o username
            boolean existe = usuarioService.listarUsuarios().stream()
                    .anyMatch(u -> u.getEmail().equals(usuario.getEmail())
                            || u.getUsername().equals(usuario.getUsername()));

            if (existe) {
                model.addAttribute("error", "El email o nombre de usuario ya está registrado");
                return "registro";
            }

            // Asignar rol y estado por defecto
            usuario.setRol("USER");
            usuario.setEstado(1);

            usuarioService.guardar(usuario);
            log.info("Usuario registrado exitosamente: {}", usuario.getUsername());

            return "redirect:/login?registro=exitoso";

        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "registro";
        }
    }
}