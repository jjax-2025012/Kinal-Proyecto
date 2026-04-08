package com.josethjax.kinalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "dpi_cliente", nullable = false)
    private String dpiCliente;

    @Column(name = "nombre_cliente", nullable = false, length = 50)
    private String nombreCliente;

    @Column(name = "apellido_cliente", nullable = false, length = 50)
    private String apellidoCliente;

    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(nullable = false)
    private int estado;

    public Cliente() {
    }

    public Cliente(String dpiCliente, String nombreCliente, String apellidoCliente, String direccion, int estado) {
        this.dpiCliente = dpiCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccion = direccion;
        this.estado = estado;
    }

    public String getDpiCliente() {
        return dpiCliente;
    }

    public void setDpiCliente(String dpiCliente) {
        this.dpiCliente = dpiCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}