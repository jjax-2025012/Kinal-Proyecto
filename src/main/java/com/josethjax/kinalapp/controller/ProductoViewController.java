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
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarProductos());
        return "producto/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("accion", "nuevo");
        return "producto/formulario";
    }

    @GetMapping("/editar/{codigo}")
    public String mostrarFormularioEditar(@PathVariable Integer codigo, Model model) {
        Producto producto = productoService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        model.addAttribute("accion", "editar");
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminarProducto(@PathVariable Integer codigo) {
        productoService.eliminar(codigo);
        return "redirect:/productos";
    }
}