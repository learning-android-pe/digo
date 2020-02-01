package com.example.digo_ec.service.model;

import android.graphics.Bitmap;


public class Item_menu {

    int Icono;

    public Bitmap getResource() {
        return resource;
    }

    public void setResource(Bitmap resource) {
        this.resource = resource;
    }

    private Bitmap resource;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    Tienda tienda;

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    String Nombre;
    String Descripcion;
    String url;


    ///ya pasado el glide
    public Item_menu(Bitmap resource, Tienda tienda, String nombre) {
        this.resource = resource;
        this.tienda = tienda;
        Nombre = nombre;
    }


    public Item_menu(int icono, String nombre, String descripcion) {
        Icono = icono;
        Nombre = nombre;
        Descripcion = descripcion;
    }

    public int getIcono() {
        return Icono;
    }

    public void setIcono(int icono) {
        Icono = icono;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
