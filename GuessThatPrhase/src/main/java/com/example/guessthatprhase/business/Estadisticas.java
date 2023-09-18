package com.example.guessthatprhase.business;

public class Estadisticas {
    private int puntuacion;
    private float tiempo_promedio;
    private int victorias;
    private int derrotas;

    public Estadisticas(int puntuacion, float tiempo_promedio, int victorias, int derrotas) {
        this.puntuacion = puntuacion;
        this.tiempo_promedio = tiempo_promedio;
        this.victorias = victorias;
        this.derrotas = derrotas;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public float getTiempo_promedio() {
        return tiempo_promedio;
    }

    public void setTiempo_promedio(float tiempo_promedio) {
        this.tiempo_promedio = tiempo_promedio;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }
}
