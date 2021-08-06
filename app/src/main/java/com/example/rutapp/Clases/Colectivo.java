package com.example.rutapp.Clases;

public class Colectivo {
    private int id;
    private String num_econom;
    private String placa;
    private String descripcion;
    private String agregado;
    private String modificado;
    private int ruta;
    private String ruta_descripcion;

    public Colectivo() {
    }

    public Colectivo(int id, String num_econom, String placa, String descripcion, String agregado, String modificado, int ruta,String ruta_descripcion) {
        this.id = id;
        this.num_econom = num_econom;
        this.placa = placa;
        this.descripcion = descripcion;
        this.agregado = agregado;
        this.modificado = modificado;
        this.ruta = ruta;
        this.ruta_descripcion = ruta_descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum_econom() {
        return num_econom;
    }

    public void setNum_econom(String num_econom) {
        this.num_econom = num_econom;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
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

    public int getRuta() {
        return ruta;
    }

    public void setRuta(int ruta) {
        this.ruta = ruta;
    }

    public String getRuta_descripcion() {
        return ruta_descripcion;
    }

    public void setRuta_descripcion(String ruta_descripcion) {
        this.ruta_descripcion = ruta_descripcion;
    }
}
