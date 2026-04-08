package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Usuario;
import com.josethjax.kinalapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorCodigo(Integer codigo) {
        return usuarioRepository.findById(codigo);
    }

    @Override
    public List<Usuario> listarPorEstado(int estado) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getEstado() == estado)
                .toList();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // Validaciones
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        // Validar que no exista otro usuario con el mismo username
        // Para nuevo usuario (codigoUsuario = 0) o para actualización
        boolean usernameExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(usuario.getUsername())
                        && u.getCodigoUsuario() != usuario.getCodigoUsuario());

        if (usernameExiste) {
            throw new IllegalArgumentException("El username ya existe");
        }

        // Validar que no exista otro usuario con el mismo email
        boolean emailExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(usuario.getEmail())
                        && u.getCodigoUsuario() != usuario.getCodigoUsuario());

        if (emailExiste) {
            throw new IllegalArgumentException("El email ya existe");
        }

        // Si no especifica rol, asignar "USER" por defecto
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            usuario.setRol("USER");
        }

        // Si no especifica estado, asignar 1 por defecto
        if (usuario.getEstado() == 0) {
            usuario.setEstado(1);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Integer codigo) {
        usuarioRepository.deleteById(codigo);
    }

    @Override
    public boolean existePorCodigo(Integer codigo) {
        return usuarioRepository.existsById(codigo);
    }

    @Override
    public Usuario actualizar(Integer codigo, Usuario usuario) {
        // Buscar el usuario existente
        Usuario usuarioExistente = usuarioRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar los campos
        usuarioExistente.setUsername(usuario.getUsername());
        usuarioExistente.setPassword(usuario.getPassword());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setRol(usuario.getRol());
        usuarioExistente.setEstado(usuario.getEstado());

        // Guardar con las validaciones
        return guardar(usuarioExistente);
    }
}