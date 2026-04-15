package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IClienteService;
import com.josethjax.kinalapp.Service.IUsuarioService;
import com.josethjax.kinalapp.Service.IVentaService;
import com.josethjax.kinalapp.entity.Cliente;
import com.josethjax.kinalapp.entity.Usuario;
import com.josethjax.kinalapp.entity.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.math.BigDecimal;

@Controller
@RequestMapping("/ventas")
public class VentaViewController {

    private static final Logger log = LoggerFactory.getLogger(VentaViewController.class);

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaViewController(IVentaService ventaService, IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        log.info("=== LISTANDO VENTAS ===");
        model.addAttribute("ventas", ventaService.listarVentas());
        return "venta/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        log.info("=== MOSTRANDO FORMULARIO NUEVA VENTA ===");
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "venta/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam("dpiCliente") String dpiCliente,
                          @RequestParam("codigoUsuario") Integer codigoUsuario) {

        log.info("=== CREANDO NUEVA VENTA ===");
        log.info("DPI Cliente: {}", dpiCliente);
        log.info("Código Usuario: {}", codigoUsuario);

        try {
            Cliente cliente = clienteService.buscarPorDPI(dpiCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + dpiCliente));

            Usuario usuario = usuarioService.buscarPorCodigo(codigoUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + codigoUsuario));

            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.now());
            // CORREGIDO: Total inicia en 0, se actualizará con los detalles
            venta.setTotal(BigDecimal.ZERO);
            venta.setEstado(1);
            venta.setCliente(cliente);
            venta.setUsuario(usuario);

            Venta guardada = ventaService.guardar(venta);
            log.info("Venta creada exitosamente con ID: {}", guardada.getCodigoVenta());

            // Redirigir al detalle para agregar productos
            return "redirect:/ventas/detalle/" + guardada.getCodigoVenta();

        } catch (Exception e) {
            log.error("ERROR al crear venta: {}", e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/anular/{codigo}")
    public String anular(@PathVariable Integer codigo) {
        log.info("=== ANULANDO VENTA ID: {} ===", codigo);
        ventaService.anular(codigo);
        return "redirect:/ventas";
    }

    @GetMapping("/detalle/{codigo}")
    public String detalle(@PathVariable Integer codigo, Model model) {
        log.info("=== MOSTRANDO DETALLE DE VENTA ID: {} ===", codigo);
        Venta venta = ventaService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + codigo));
        model.addAttribute("venta", venta);
        return "venta/detalle";
    }
}