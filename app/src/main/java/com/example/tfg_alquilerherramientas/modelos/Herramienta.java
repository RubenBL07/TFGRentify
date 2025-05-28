package com.example.tfg_alquilerherramientas.modelos;

import java.io.Serializable;
import java.math.BigDecimal;

public class Herramienta implements Serializable {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioDia;
    private String categoria;
    private Boolean disponible;
    private String imagenUrl;

    public Herramienta(Long id, String nombre, String descripcion, BigDecimal precioDia, String categoria, Boolean disponible, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioDia = precioDia;
        this.categoria = categoria;
        this.disponible = disponible;
        this.imagenUrl = imagenUrl;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(BigDecimal precioDia) {
        this.precioDia = precioDia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
