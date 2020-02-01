package com.example.digo_ec.service.model;

import java.util.ArrayList;
import java.util.List;

public class Tienda {
    String propietario, descripcion,telefono, direccion, ciudad, pais,provincia;
    String cedula;
    String nombre="";
    String imagen="";
    String calle="";
    String ciudadela="";

    String direccionExacta="";



    String id="";
    String portada="";
    String categoria, subcategoria;
    int cantidad_seguidores=0;
    int vecesValorada=0;
    float AcumulacionValoraciones=0.0f;
    List<String> listado_promociones= new ArrayList<>();
    List<String> listado_productos= new ArrayList<>();
    List<String> listado_horario= new ArrayList<>();

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public List<String> getListado_horario() {
        return listado_horario;
    }

    public void setListado_horario(List<String> listado_horario) {
        this.listado_horario = listado_horario;
    }

    public String getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(String ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

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

    public float getAcumulacionValoraciones() {
        return AcumulacionValoraciones;
    }

    public void setAcumulacionValoraciones(float acumulacionValoraciones) {
        AcumulacionValoraciones = acumulacionValoraciones;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public int getCantidad_seguidores() {
        return cantidad_seguidores;
    }

    public void setCantidad_seguidores(int cantidad_seguidores) {
        this.cantidad_seguidores = cantidad_seguidores;
    }

    public List<String> getListado_productos() {
        return listado_productos;
    }

    public void setListado_productos(List<String> listado_productos) {
        this.listado_productos = listado_productos;
    }

    public List<String> getListado_promociones() {
        return listado_promociones;
    }

    public void setListado_promociones(List<String> listado_promociones) {
        this.listado_promociones = listado_promociones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }


    double lat, lng;




    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getVecesValorada() {
        return vecesValorada;
    }

    public void setVecesValorada(int vecesValorada) {
        this.vecesValorada = vecesValorada;
    }
    public String getDireccionExacta() {
        return direccionExacta;
    }

    public void setDireccionExacta(String direccionExacta) {
        this.direccionExacta = direccionExacta;
    }

}
