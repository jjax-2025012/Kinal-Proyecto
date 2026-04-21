package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IDetalleVentaService;
import com.josethjax.kinalapp.entity.DetalleVenta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-venta")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar() {
        List<DetalleVenta> detalles = detalleVentaService.listarDetalles();
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<DetalleVenta> buscarPorCodigo(@PathVariable Integer codigo) {
        return detalleVentaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/venta/{codigoVenta}")
    public ResponseEntity<List<DetalleVenta>> listarPorVenta(@PathVariable Integer codigoVenta) {
        List<DetalleVenta> detalles = detalleVentaService.listarPorVenta(codigoVenta);
        return ResponseEntity.ok(detalles);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta nuevoDetalle = detalleVentaService.guardar(detalleVenta);
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody DetalleVenta detalleVenta) {
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }

            DetalleVenta detalleActualizado = detalleVentaService.actualizar(codigo, detalleVenta);
            return ResponseEntity.ok(detalleActualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            detalleVentaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}