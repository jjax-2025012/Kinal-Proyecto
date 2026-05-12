package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IVentaService;
import com.josethjax.kinalapp.entity.Venta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        List<Venta> ventas = ventaService.listarVentas();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Venta> buscarPorCodigo(@PathVariable Integer codigo) {
        return ventaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Venta>> porEstado(@PathVariable int estado) {
        List<Venta> lista = ventaService.listarPorEstado(estado);
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.guardar(venta);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        try {
            if (!ventaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            ventaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody Venta venta) {
        try {
            if (!ventaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            Venta ventaActualizada = ventaService.actualizar(codigo, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}/anular")
    public ResponseEntity<?> anular(@PathVariable Integer codigo) {
        try {
            if (!ventaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            Venta ventaAnulada = ventaService.anular(codigo);
            return ResponseEntity.ok(ventaAnulada);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}