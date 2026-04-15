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

        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);

        model.addAttribute("detalle", detalle);
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("codigoVenta", codigoVenta);
        return "detalle-venta/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer codigoVenta,
                          @RequestParam Integer productoId,
                          @RequestParam Integer cantidad,
                          @RequestParam BigDecimal precioUnitario) {

        Venta venta = ventaService.buscarPorCodigo(codigoVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Producto producto = productoService.buscarPorCodigo(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);

        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        detalle.setSubtotal(subtotal);

        detalleVentaService.guardar(detalle);

        return "redirect:/ventas/detalle/" + codigoVenta;
    }
}