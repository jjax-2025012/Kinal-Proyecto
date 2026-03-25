package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.Service.IProductoService;
import com.josethjax.kinalapp.entity.Producto;
import com.josethjax.kinalapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorCodigo(Integer codigo) {
        return productoRepository.findById(codigo);
    }

    @Override
    public List<Producto> listarPorEstado(int estado) {
        return productoRepository.findAll().stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarConStock() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0 && p.getEstado() == 1)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarSinStock() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStock() == 0 && p.getEstado() == 1)
                .collect(Collectors.toList());
    }

    @Override
    public Producto guardar(Producto producto) {
        // Validaciones
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        if (producto.getStock() == null) {
            producto.setStock(0);
        }

        if (producto.getEstado() == null) {
            producto.setEstado(1); // Activo por defecto
        }

        // Validar que no exista otro producto con el mismo nombre
        boolean nombreExiste = productoRepository.findAll().stream()
                .anyMatch(p -> p.getNombreProducto().equalsIgnoreCase(producto.getNombreProducto())
                        && !p.getCodigoProducto().equals(producto.getCodigoProducto()));

        if (nombreExiste) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }

        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Integer codigo) {
        productoRepository.deleteById(codigo);
    }

    @Override
    public boolean existePorCodigo(Integer codigo) {
        return productoRepository.existsById(codigo);
    }

    @Override
    public Producto actualizar(Integer codigo, Producto producto) {
        Producto productoExistente = productoRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoExistente.setNombreProducto(producto.getNombreProducto());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        productoExistente.setEstado(producto.getEstado());

        return guardar(productoExistente);
    }

    @Override
    public void actualizarStock(Integer codigo, int cantidad) {
        Producto producto = productoRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int nuevoStock = producto.getStock() - cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + producto.getStock());
        }

        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }
}