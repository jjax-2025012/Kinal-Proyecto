package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    List<DetalleVenta> listarDetalles();

    Optional<DetalleVenta> buscarPorCodigo(Integer codigo);

    List<DetalleVenta> listarPorVenta(Integer codigoVenta);

    DetalleVenta guardar(DetalleVenta detalleVenta);

    DetalleVenta actualizar(Integer codigo, DetalleVenta detalleVenta);

    void eliminar(Integer codigo);

    boolean existePorCodigo(Integer codigo);
}