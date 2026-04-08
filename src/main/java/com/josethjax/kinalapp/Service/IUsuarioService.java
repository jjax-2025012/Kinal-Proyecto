package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarUsuarios();

    Optional<Usuario> buscarPorCodigo(Integer codigo);

    List<Usuario> listarPorEstado(int estado);

    Usuario guardar(Usuario usuario);

    void eliminar(Integer codigo);

    boolean existePorCodigo(Integer codigo);

    Usuario actualizar(Integer codigo, Usuario usuario);

}