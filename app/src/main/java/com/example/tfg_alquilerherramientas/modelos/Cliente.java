package com.example.tfg_alquilerherramientas.modelos;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cliente implements Serializable {
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private String direccion;
    private BigDecimal saldo;

    public Cliente(Long id, String nombre, String email, String password, String direccion, BigDecimal saldo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.direccion = direccion;
        this.saldo = saldo;
    }

    public Cliente(String nombre, String email, String password, String direccion) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.direccion = direccion;
    }

    public Cliente() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
