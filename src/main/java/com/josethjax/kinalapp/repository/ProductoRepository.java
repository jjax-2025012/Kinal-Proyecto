package com.josethjax.kinalapp.repository;

import com.josethjax.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
