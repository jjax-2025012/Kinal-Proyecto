package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IUsuarioService;
import com.josethjax.kinalapp.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioViewController.class);

    private final IUsuarioService usuarioService;

    public UsuarioViewController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        log.info("=== LISTANDO USUARIOS ===");
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "usuario/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        log.info("=== MOSTRANDO FORMULARIO NUEVO USUARIO ===");
        model.addAttribute("usuario", new Usuario());
        return "usuario/formulario";
    }

    @GetMapping("/editar/{codigo}")
    public String editar(@PathVariable Integer codigo, Model model) {
        log.info("=== EDITANDO USUARIO ID: {} ===", codigo);
        Usuario usuario = usuarioService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + codigo));
        model.addAttribute("usuario", usuario);
        return "usuario/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {
        log.info("=== GUARDANDO USUARIO: {} ===", usuario.getUsername());
        log.info("Datos recibidos - Username: {}, Email: {}, Rol: {}, Estado: {}",
                usuario.getUsername(), usuario.getEmail(), usuario.getRol(), usuario.getEstado());

        try {
            // Validaciones explícitas
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Username es obligatorio");
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password es obligatorio");
            }
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email es obligatorio");
            }
            if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
                usuario.setRol("USER");
            }
            if (usuario.getEstado() != 1 && usuario.getEstado() != 0) {
                usuario.setEstado(1);
            }

            Usuario guardado = usuarioService.guardar(usuario);
            log.info("Usuario guardado exitosamente con ID: {}", guardado.getCodigoUsuario());

        } catch (Exception e) {
            log.error("ERROR al guardar usuario: {}", e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable Integer codigo) {
        log.info("=== ELIMINANDO USUARIO ID: {} ===", codigo);
        usuarioService.eliminar(codigo);
        return "redirect:/usuarios";
    }
}