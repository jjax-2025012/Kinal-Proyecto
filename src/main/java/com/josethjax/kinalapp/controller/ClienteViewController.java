package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IClienteService;
import com.josethjax.kinalapp.entity.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteViewController.class);

    private final IClienteService clienteService;

    public ClienteViewController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarClientes(Model model) {
        logger.info("Listando todos los clientes");
        model.addAttribute("clientes", clienteService.listarClientes());
        return "cliente/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        logger.info("Mostrando formulario para nuevo cliente");
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("accion", "nuevo");
        return "cliente/formulario";
    }

    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioEditar(@PathVariable String dpi, Model model) {
        logger.info("Buscando cliente con DPI: {}", dpi);
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DPI: " + dpi));
        model.addAttribute("cliente", cliente);
        model.addAttribute("accion", "editar");
        return "cliente/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        logger.info("Guardando cliente: {}", cliente.getDpiCliente());
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminarCliente(@PathVariable String dpi) {
        logger.info("Eliminando cliente con DPI: {}", dpi);
        clienteService.eliminar(dpi);
        return "redirect:/clientes";
    }
}