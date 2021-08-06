package com.example.rutapp.Clases;

public class Ubicacion {
    private int id;
    private Double latitud;
    private Double longitud;
    private int Colectivo;

    public Ubicacion(int id, Double latitud, Double longitud, int colectivo) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        Colectivo = colectivo;
    }

    public Ubicacion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getColectivo() {
        return Colectivo;
    }

    public void setColectivo(int colectivo) {
        Colectivo = colectivo;
    }
}
