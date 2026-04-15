package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IProductoService;
import com.josethjax.kinalapp.entity.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoViewController {

    private final IProductoService productoService;

    public ProductoViewController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarProductos());
        return "producto/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/formulario";
    }

    @GetMapping("/editar/{codigo}")
    public String editar(@PathVariable Integer codigo, Model model) {
        Producto producto = productoService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        // Asegurar que el estado tenga valor
        if (producto.getEstado() != 1 && producto.getEstado() != 0) {
            producto.setEstado(1);
        }
        // Asegurar que el stock no sea negativo
        if (producto.getStock() == null || producto.getStock() < 0) {
            producto.setStock(0);
        }
        // Asegurar que el precio no sea nulo
        if (producto.getPrecio() == null) {
            producto.setPrecio(java.math.BigDecimal.ZERO);
        }
        productoService.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable Integer codigo) {
        productoService.eliminar(codigo);
        return "redirect:/productos";
    }
}