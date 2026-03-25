package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarProductos();

    Optional<Producto> buscarPorCodigo(Integer codigo);

    List<Producto> listarPorEstado(int estado);

    List<Producto> listarConStock(); // Productos con stock > 0

    List<Producto> listarSinStock(); // Productos con stock = 0

    Producto guardar(Producto producto);

    void eliminar(Integer codigo)
            ;
    boolean existePorCodigo(Integer codigo);

    Producto actualizar(Integer codigo, Producto producto);

    void actualizarStock(Integer codigo, int cantidad); // Para ventas
}