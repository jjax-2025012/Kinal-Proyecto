package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Producto;
import com.josethjax.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(Integer codigoProducto) {
        return productoRepository.findById(codigoProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorEstado(int estado) {
        return listarProductos().stream()
                .filter(p -> p.getEstado() == estado)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarConStock() {
        return listarProductos().stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarSinStock() {
        return listarProductos().stream()
                .filter(p -> p.getStock() != null && p.getStock() == 0)
                .toList();
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);

        if (producto.getEstado() == null || producto.getEstado() == 0) {
            producto.setEstado(1);
        }

        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Integer codigoProducto, Producto producto) {
        if (!productoRepository.existsById(codigoProducto)) {
            throw new RuntimeException("El producto no se encontró con el código: " + codigoProducto);
        }

        producto.setCodigoProducto(codigoProducto);
        validarProducto(producto);

        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Integer codigoProducto) {
        if (!productoRepository.existsById(codigoProducto)) {
            throw new RuntimeException("El producto no se encontró con el código: " + codigoProducto);
        }
        productoRepository.deleteById(codigoProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigoProducto) {
        return productoRepository.existsById(codigoProducto);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}