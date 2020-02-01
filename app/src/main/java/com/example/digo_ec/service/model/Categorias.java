package com.example.digo_ec.service.model;

import java.util.ArrayList;
import java.util.List;

public class Categorias {


    private  String imagen_color;
    private  String imagen_sincolor;

    private String nombre_categoria;
    private  List<String> subcategoria= new ArrayList<>();
    private  String tipo;

    public String getImagen_color() {
        return imagen_color;
    }

    public void setImagen_color(String imagen_color) {
        this.imagen_color = imagen_color;
    }

    public String getImagen_sincolor() {
        return imagen_sincolor;
    }

    public void setImagen_sincolor(String imagen_sincolor) {
        this.imagen_sincolor = imagen_sincolor;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    public List<String> getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(List<String> subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
