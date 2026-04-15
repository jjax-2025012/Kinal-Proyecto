package com.josethjax.kinalapp.repository;

import com.josethjax.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {  // Cambiado de Integer a Integer (ya está bien)
}