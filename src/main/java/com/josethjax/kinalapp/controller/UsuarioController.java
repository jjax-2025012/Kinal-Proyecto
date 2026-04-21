package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IUsuarioService;
import com.josethjax.kinalapp.entity.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Usuario> buscarPorCodigo(@PathVariable Integer codigo) {
        return usuarioService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> porEstado(@PathVariable int estado) {
        List<Usuario> lista = usuarioService.listarPorEstado(estado);
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            usuarioService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("El controlador funciona!");
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody Usuario usuario) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuarioActualizado = usuarioService.actualizar(codigo, usuario);
            return ResponseEntity.ok(usuarioActualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}