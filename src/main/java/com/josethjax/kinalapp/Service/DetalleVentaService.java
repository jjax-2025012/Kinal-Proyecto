package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.Service.IDetalleVentaService;
import com.josethjax.kinalapp.Service.IProductoService;
import com.josethjax.kinalapp.Service.IVentaService;
import com.josethjax.kinalapp.entity.DetalleVenta;
import com.josethjax.kinalapp.entity.Producto;
import com.josethjax.kinalapp.entity.Venta;
import com.josethjax.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final IVentaService ventaService;
    private final IProductoService productoService;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository,
                               IVentaService ventaService,
                               IProductoService productoService) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarDetalles() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(Integer codigo) {
        return detalleVentaRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarPorVenta(Integer codigoVenta) {
        return listarDetalles().stream()
                .filter(d -> d.getVenta() != null && d.getVenta().getCodigoVenta().equals(codigoVenta))
                .toList();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalleVenta(detalleVenta);

        // Calcular subtotal si no viene
        if (detalleVenta.getSubtotal() == null) {
            BigDecimal subtotal = detalleVenta.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleVenta.getCantidad()));
            detalleVenta.setSubtotal(subtotal);
        }

        // Actualizar stock del producto
        productoService.actualizarStock(
                detalleVenta.getProducto().getCodigoProducto(),
                detalleVenta.getCantidad()
        );

        DetalleVenta detalleGuardado = detalleVentaRepository.save(detalleVenta);

        // Actualizar total de la venta
        actualizarTotalVenta(detalleVenta.getVenta().getCodigoVenta());

        return detalleGuardado;
    }

    @Override
    public DetalleVenta actualizar(Integer codigo, DetalleVenta detalleVenta) {
        DetalleVenta detalleExistente = detalleVentaRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado"));

        // Si cambia la cantidad, ajustar stock
        if (!detalleExistente.getCantidad().equals(detalleVenta.getCantidad())) {
            int diferencia = detalleVenta.getCantidad() - detalleExistente.getCantidad();
            productoService.actualizarStock(
                    detalleVenta.getProducto().getCodigoProducto(),
                    diferencia
            );
        }

        detalleExistente.setCantidad(detalleVenta.getCantidad());
        detalleExistente.setPrecioUnitario(detalleVenta.getPrecioUnitario());

        // Recalcular subtotal
        BigDecimal subtotal = detalleVenta.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalleVenta.getCantidad()));
        detalleExistente.setSubtotal(subtotal);

        DetalleVenta detalleActualizado = detalleVentaRepository.save(detalleExistente);

        // Actualizar total de la venta
        actualizarTotalVenta(detalleExistente.getVenta().getCodigoVenta());

        return detalleActualizado;
    }

    @Override
    public void eliminar(Integer codigo) {
        DetalleVenta detalle = detalleVentaRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado"));

        Integer codigoVenta = detalle.getVenta().getCodigoVenta();
        Integer codigoProducto = detalle.getProducto().getCodigoProducto();
        Integer cantidad = detalle.getCantidad();

        detalleVentaRepository.deleteById(codigo);

        // Devolver stock
        productoService.actualizarStock(codigoProducto, -cantidad);

        // Actualizar total de la venta
        actualizarTotalVenta(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        return detalleVentaRepository.existsById(codigo);
    }

    private void validarDetalleVenta(DetalleVenta detalleVenta) {
        if (detalleVenta.getCantidad() == null || detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detalleVenta.getPrecioUnitario() == null ||
                detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }

        if (detalleVenta.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }

        if (detalleVenta.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        // Verificar que la venta existe
        if (!ventaService.existePorCodigo(detalleVenta.getVenta().getCodigoVenta())) {
            throw new IllegalArgumentException("La venta no existe");
        }

        // Verificar que el producto existe
        if (!productoService.existePorCodigo(detalleVenta.getProducto().getCodigoProducto())) {
            throw new IllegalArgumentException("El producto no existe");
        }
    }

    private void actualizarTotalVenta(Integer codigoVenta) {
        Optional<Venta> ventaOpt = ventaService.buscarPorCodigo(codigoVenta);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();

            // Sumar todos los subtotales de los detalles de esta venta
            BigDecimal total = listarPorVenta(codigoVenta).stream()
                    .map(DetalleVenta::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            venta.setTotal(total);
            ventaService.guardar(venta);
        }
    }
}