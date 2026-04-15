package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IDetalleVentaService;
import com.josethjax.kinalapp.Service.IProductoService;
import com.josethjax.kinalapp.Service.IVentaService;
import com.josethjax.kinalapp.entity.DetalleVenta;
import com.josethjax.kinalapp.entity.Producto;
import com.josethjax.kinalapp.entity.Venta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/detalles-venta")
public class DetalleVentaViewController {

    private final IDetalleVentaService detalleVentaService;
    private final IVentaService ventaService;
    private final IProductoService productoService;

    public DetalleVentaViewController(IDetalleVentaService detalleVentaService,
                                      IVentaService ventaService,
                                      IProductoService productoService) {
        this.detalleVentaService = detalleVentaService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @GetMapping("/nuevo/{codigoVenta}")
    public String nuevo(@PathVariable Integer codigoVenta, Model model) {
        Venta venta = ventaService.buscarPorCodigo(codigoVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        model.addAttribute("codigoVenta", codigoVenta);
        model.addAttribute("productos", productoService.listarProductos());
        return "detalle-venta/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer codigoVenta,
                          @RequestParam Integer productoId,
                          @RequestParam Integer cantidad,
                          @RequestParam BigDecimal precioUnitario) {

        // Buscar la venta
        Venta venta = ventaService.buscarPorCodigo(codigoVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Buscar el producto
        Producto producto = productoService.buscarPorCodigo(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Crear el detalle de venta en el mismo formato que Postman
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setVenta(venta);
        detalle.setProducto(producto);

        // El subtotal se calcula automáticamente en el servicio
        // El stock se actualiza automáticamente en el servicio
        // El total de la venta se actualiza automáticamente en el servicio

        detalleVentaService.guardar(detalle);

        return "redirect:/ventas/detalle/" + codigoVenta;
    }
}