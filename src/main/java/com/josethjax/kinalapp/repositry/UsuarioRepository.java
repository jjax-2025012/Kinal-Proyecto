package com.josethjax.kinalapp.repositry;

import com.josethjax.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository <Usuario, Integer>{

}
