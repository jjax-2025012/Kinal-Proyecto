package com.josethjax.kinalapp.repositry;

import com.josethjax.kinalapp.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository <Cliente,String>{


}
