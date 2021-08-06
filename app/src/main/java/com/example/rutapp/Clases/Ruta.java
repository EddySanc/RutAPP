package com.example.rutapp.Clases;

public class Ruta {

    private int id;
    private int num_ruta;
    private String descripcion;
    private String agregado;
    private String modificado;

    public Ruta() {
    }

    public Ruta(int id, int num_ruta, String descripcion, String agregado, String modificado) {
        this.id = id;
        this.num_ruta = num_ruta;
        this.descripcion = descripcion;
        this.agregado = agregado;
        this.modificado = modificado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum_ruta() {
        return num_ruta;
    }

    public void setNum_ruta(int num_ruta) {
        this.num_ruta = num_ruta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAgregado() {
        return agregado;
    }

    public void setAgregado(String agregado) {
        this.agregado = agregado;
    }

    public String getModificado() {
        return modificado;
    }

    public void setModificado(String modificado) {
        this.modificado = modificado;
    }
}
