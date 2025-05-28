package com.example.tfg_alquilerherramientas.modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class Reserva implements Serializable {
    private Long id;
    private Cliente cliente;
    private Herramienta herramienta;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal precioTotal;
    private String estado;

    public Reserva(Long id, Cliente cliente, Herramienta herramienta, LocalDateTime fechaInicio, LocalDateTime fechaFin, BigDecimal precioTotal, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.herramienta = herramienta;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioTotal = precioTotal;
        this.estado = estado;
    }

    public Reserva(Cliente cliente, Herramienta herramienta, LocalDateTime fechaInicio, String estado) {
        this.cliente = cliente;
        this.herramienta = herramienta;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Herramienta getHerramienta() {
        return herramienta;
    }

    public void setHerramienta(Herramienta herramienta) {
        this.herramienta = herramienta;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
