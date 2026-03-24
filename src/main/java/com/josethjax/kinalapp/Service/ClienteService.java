package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Cliente;
import com.josethjax.kinalapp.repositry.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/*
 * Anotación que registra un bean
 * Que la clase contiene la logica del negocio
 * */
@Service
/*
 * Por defecto, todos los metodos de esta clase seran transaccionales
 * Una Transaccion es que puede o no ocurrir algo
 * */
@Transactional

public class ClienteService implements IClienteService{

    /*
     * private: solo es accesible dentro de la misma clase
     * final: no puede cambiar, es constante
     * ClienteRepository: El repositorio para acceder a la BD
     * Inyección de Dependencia ya q spring nos da el repositorio
     * */

    private final ClienteRepository clienteRepository;

    //Constructor: este se ejecuta al crear un objeto, Spring pasa el repositorio automaticamente (Inyeccion de dependencia=

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        //Asignar el repositorio a nuestra variable clase
    }

    //Indica que se esta implementando un metodo de la interfaz
    @Override
    //Optimiza la consulta, solo lectura, pero que no bloquee la BD
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
        //find all es un metodo de spring q hace un select * from clientes
        //este metodo es de JpaRepository
    }


    @Override
    public Cliente guardar(Cliente cliente) {
        /*
         * Metodo guardar crea un cliente
         * aca es donde colocamos la logica del negocio antes de guardar
         * primero validamos el dato
         * */

        validarCliente(cliente);
        if (cliente.getEstado() == 0){
            cliente.setEstado(1);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Busca un cliente por DPI
        return clienteRepository.findById(dpi);
        //Optional evita el Null Pointer Exception
    }


    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Metodo para actualizar un cliente existente

        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontró por el DPI: " + dpi);
            //Si no existe se lanza una excepcion (error controlado)
        }
        cliente.setDpiCliente(dpi);
        //Asegurarnos que el DPI del objeto coincida con el de la URL
        //Por seguridad usamos el DPI de la URL y no el que viene en el JSon
        validarCliente(cliente);

        return clienteRepository.save(cliente);
        //Save no solo sirve para guardar, si no para actualizar
        //Si el dato existe solo hace update, pero si no lo crea
    }


    @Override
    public void eliminar(String dpi) {
        //Eliminar un cliente
        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontró con el DPI: " + dpi);
        }
        clienteRepository.deleteById(dpi);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        //verificar si el cliente existe
        return clienteRepository.existsById(dpi);
    }

    //metodo privado (solo puede utilizarse dentro de la clase)
    private void validarCliente(Cliente cliente){
        /*
         * Validaciones del negocio: Este metodo se hara privado pq es algo
         * interno del servicio
         * */

        if (cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()){
            //Si el dpi es null o esta vacio despues de quitar los espacios
            //Lanza una excepcion con un mensaje
            throw new IllegalArgumentException(("El DPI es un dato obligatorio")
            );
        }

        if(cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException(("El nombre es un dato obligatorio")
            );
        }

        if(cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException(("El apellido es un dato obligatorio")
            );
        }
    }


    // Parámetro: estado - 1 = activo, 2 = inactivo
    // Retorna: una lista de objetos Cliente que tienen el estado especificado
    //.stream: Convierte la lista en un flujo de datos que permite aplicar filtros
    //.filter : filtra todos los datos que cumplen con las condiciones
    //c -> c.getEstado() == estado: Es una expresión lambda, verifica si su estado coincide con el que pasa como parámetro.
    //.toList(): Convierte el stream filtrado de vuelta a una List<Cliente>
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarPorEstado(int estado) {
        return listarClientes().stream()
                .filter(c -> c.getEstado() == estado)
                .toList();
    }

}
