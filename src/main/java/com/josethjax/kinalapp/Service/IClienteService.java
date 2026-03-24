package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    /*
     * Interfaz: es un contrato que dice que metodos debe tener
     * cualquier servicio de Clientes, no tiene implementación, solo la definición de los metodos
     * */

    //Metodo que duelve una lista de todos los clientes
    List<Cliente> listarClientes();

    //Metodo que devuelva una lista dependiendo del estado del cliente
    List<Cliente> listarPorEstado(int estado);


    /*
     * List hace que devuelva una lista de objetos de la entidad Cliente
     * */

    //Metodo que guarda un cliente en la base de datos
    Cliente guardar(Cliente cliente);
    //Parametros: recibe un objeto Cliente con los datos a guardar

    //Optional - Contenedor que puede o no tener valor
    //Evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    //Metodo q actualiza un cliente
    Cliente actualizar(String dpi, Cliente cliente);

    /*
     * Parametros: DPI es el dpi del cliente a actualizar
     * el Cliente cliente es un objeto de tipo cliente con los datos nuevos
     * Retorna un objeto de tipo cliente ya actualizado
     * */

    /*
     * Metodo de tipo void para eliminar a un cliente
     * void: no retorna ningun valor o dato
     * Elimina un cliente por su dpi
     * */

    void eliminar(String dpi);

    //Boolean - Retornar True si existe y False si no existe
    boolean existePorDPI(String dpi);
}
