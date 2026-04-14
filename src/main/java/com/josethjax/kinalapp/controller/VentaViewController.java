package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IClienteService;
import com.josethjax.kinalapp.Service.IUsuarioService;
import com.josethjax.kinalapp.Service.IVentaService;
import com.josethjax.kinalapp.entity.Venta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/ventas")
public class VentaViewController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaViewController(IVentaService ventaService,
                               IClienteService clienteService,
                               IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarVentas(Model model) {
        model.addAttribute("ventas", ventaService.listarVentas());
        return "venta/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("accion", "nuevo");
        return "venta/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta) {
        venta.setFechaVenta(LocalDate.now());
        venta.setTotal(java.math.BigDecimal.ZERO);
        venta.setEstado(1);
        ventaService.guardar(venta);
        return "redirect:/ventas";
    }

    @GetMapping("/anular/{codigo}")
    public String anularVenta(@PathVariable Integer codigo) {
        ventaService.anular(codigo);
        return "redirect:/ventas";
    }

    @GetMapping("/detalle/{codigo}")
    public String verDetalleVenta(@PathVariable Integer codigo, Model model) {
        Venta venta = ventaService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        model.addAttribute("venta", venta);
        return "venta/detalle";
    }
}