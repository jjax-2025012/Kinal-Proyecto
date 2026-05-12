package com.josethjax.kinalapp.Service;

import com.josethjax.kinalapp.entity.Cliente;
import com.josethjax.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService{

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        validarCliente(cliente);
        // CORREGIDO: Ya no fuerza estado=1, respeta el valor enviado
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        return clienteRepository.findById(dpi);
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontró por el DPI: " + dpi);
        }
        cliente.setDpiCliente(dpi);
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontró con el DPI: " + dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        return clienteRepository.existsById(dpi);
    }

    private void validarCliente(Cliente cliente){
        if (cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }
        if(cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }
        if(cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarPorEstado(int estado) {
        return listarClientes().stream()
                .filter(c -> c.getEstado() == estado)
                .toList();
    }
}