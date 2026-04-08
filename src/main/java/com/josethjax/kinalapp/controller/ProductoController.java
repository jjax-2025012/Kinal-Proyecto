package com.josethjax.kinalapp.controller;

import com.josethjax.kinalapp.Service.IProductoService;
import com.josethjax.kinalapp.entity.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productoService.listarProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> buscarPorCodigo(@PathVariable Integer codigo) {
        return productoService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> porEstado(@PathVariable int estado) {
        List<Producto> lista = productoService.listarPorEstado(estado);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/con-stock")
    public ResponseEntity<List<Producto>> conStock() {
        List<Producto> lista = productoService.listarConStock();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/sin-stock")
    public ResponseEntity<List<Producto>> sinStock() {
        List<Producto> lista = productoService.listarSinStock();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.guardar(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        try {
            if (!productoService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            productoService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody Producto producto) {
        try {
            if (!productoService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }

            Producto productoActualizado = productoService.actualizar(codigo, producto);
            return ResponseEntity.ok(productoActualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}