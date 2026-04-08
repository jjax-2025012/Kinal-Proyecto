package com.josethjax.kinalapp.security;

import com.josethjax.kinalapp.entity.Usuario;
import com.josethjax.kinalapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Buscando usuario: " + username);

        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        System.out.println("Usuario encontrado: " + usuario.getUsername());
        System.out.println("Rol: " + usuario.getRol());
        System.out.println("Password en BD: " + usuario.getPassword());

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
        );
    }

}