package com.example.rutapp.Clases;

public class Recorrido {

    private int id;
    private int orden;
    private Double latitud;
    private Double longitud;
    private int ruta;

    public Recorrido(int id, int orden, Double latitud, Double longitud, int ruta) {
        this.id = id;
        this.orden = orden;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ruta = ruta;
    }

    public Recorrido() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public int getRuta() {
        return ruta;
    }

    public void setRuta(int ruta) {
        this.ruta = ruta;
    }
}
