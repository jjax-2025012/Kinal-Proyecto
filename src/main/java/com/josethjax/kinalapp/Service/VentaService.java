package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Venta;
import com.josethjax.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigo(Integer codigoVenta) {
        return ventaRepository.findById(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarPorEstado(int estado) {
        return listarVentas().stream()
                .filter(v -> v.getEstado() == estado)
                .toList();
    }

    @Override
    public Venta guardar(Venta venta) {
        validarVenta(venta);

        if (venta.getEstado() == null || venta.getEstado() == 0) {
            venta.setEstado(1);
        }

        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizar(Integer codigoVenta, Venta venta) {
        if (!ventaRepository.existsById(codigoVenta)) {
            throw new RuntimeException("La venta no se encontró con el código: " + codigoVenta);
        }

        venta.setCodigoVenta(codigoVenta);
        validarVenta(venta);

        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Integer codigoVenta) {
        if (!ventaRepository.existsById(codigoVenta)) {
            throw new RuntimeException("La venta no se encontró con el código: " + codigoVenta);
        }
        ventaRepository.deleteById(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigoVenta) {
        return ventaRepository.existsById(codigoVenta);
    }

    private void validarVenta(Venta venta) {
        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha de venta es obligatoria");
        }

        if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total debe ser un valor válido mayor o igual a 0");
        }

        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (venta.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
    }
}