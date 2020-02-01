package com.example.digo_ec.service.model;

import java.util.Date;

public class SugerenciaTipo {
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getMejora() {
        return mejora;
    }

    public void setMejora(String mejora) {
        this.mejora = mejora;
    }

    Date fecha;
    String categoria;
    String subcategoria;
    String mejora;
    String correo;

    public String getNombre_negocio() {
        return nombre_negocio;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombre_negocio(String nombre_negocio) {
        this.nombre_negocio = nombre_negocio;
    }

    String nombre_negocio;

    public String getFechita() {
        return fechita;
    }

    public void setFechita(String fechita) {
        this.fechita = fechita;
    }

    String fechita;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
