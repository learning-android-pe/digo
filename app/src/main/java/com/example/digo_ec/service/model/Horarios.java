package com.example.digo_ec.service.model;

public class Horarios {
    private  int dia;
    private  String apertura;
    private  String cierre;
    private boolean atencion=false;


    public Horarios(int dia, String apertura, String cierre, boolean atencion) {
        this.dia = dia;
        this.apertura = apertura;
        this.cierre = cierre;
        this.atencion = atencion;
    }

    public boolean isAtencion() {
        return atencion;
    }
    public void setAtencion(boolean atencion) {
        this.atencion = atencion;
    }
    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getApertura() {
        return apertura;
    }

    public void setApertura(String apertura) {
        this.apertura = apertura;
    }

    public String getCierre() {
        return cierre;
    }

    public void setCierre(String cierre) {
        this.cierre = cierre;
    }



    public String fusion(){
        String fusion=""+dia+"-"+atencion+"-"+apertura+"-"+cierre;
        return  fusion;
    }
}
