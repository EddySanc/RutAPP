package com.example.rutapp.Clases;

public class Solicitudes {
    private int id;
    private String identificador;
    private Double latitud;
    private Double longitud;
    private int ruta;

    public Solicitudes(int id, String identificador, Double latitud, Double longitud, int ruta) {
        this.id = id;
        this.identificador = identificador;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ruta = ruta;
    }

    public Solicitudes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
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
