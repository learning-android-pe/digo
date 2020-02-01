package com.example.digo_ec.service.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    String nombre, apellido, correo,token, foto_perfil;
    int monedas=0;
    int creados=0;

    public int getCreados() {
        return creados;
    }

    public void setCreados(int creados) {
        this.creados = creados;
    }

    List<String> listado_tiendas= new ArrayList<>();

    public List<String> getListado_tiendas() {
        return listado_tiendas;
    }

    public void setListado_tiendas(List<String> listado_tiendas) {
        this.listado_tiendas = listado_tiendas;
    }
    public String getFoto_perfil() {
        return foto_perfil;
    }



    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    boolean tiene_tienda;

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public String getNombre() {
        return nombre;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isTiene_tienda() {
        return tiene_tienda;
    }

    public void setTiene_tienda(boolean tiene_tienda) {
        this.tiene_tienda = tiene_tienda;
    }

}
