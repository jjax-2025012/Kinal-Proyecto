package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IClienteService;
import com.josethjax.kinalapp.entity.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    private final IClienteService clienteService;

    public ClienteViewController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "cliente/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("accion", "nuevo");
        return "cliente/formulario";
    }

    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioEditar(@PathVariable String dpi, Model model) {
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("accion", "editar");
        return "cliente/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminarCliente(@PathVariable String dpi) {
        clienteService.eliminar(dpi);
        return "redirect:/clientes";
    }
}