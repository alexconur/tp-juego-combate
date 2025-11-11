package org.vista.tipos;

public class UbicacionInicio {
    private final int fila;
    private final int columna;

    public UbicacionInicio(int fila, int columna) { 
        this.fila = fila; 
        this.columna = columna; 
    }
    
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
}