package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {

    List<Venta> listarVentas();

    List<Venta> listarPorEstado(int estado);

    Venta guardar(Venta venta);

    Optional<Venta> buscarPorCodigo(Integer codigoVenta);

    Venta actualizar(Integer codigoVenta, Venta venta);

    void eliminar(Integer codigoVenta);

    boolean existePorCodigo(Integer codigoVenta);

    Venta anular(Integer codigoVenta);
}