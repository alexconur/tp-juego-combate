package org.modelo.tablero;

import org.modelo.unidades.Unidad;

public abstract class Casilla {
    private int fila;
    private int col;
    private Unidad ocupante;

    protected Casilla(int fila, int columna) {
        this.fila = fila;
        this.col = columna;
        this.ocupante = null;
    }

    public abstract boolean esTransitable();

    public abstract void efectos();

    public int getFila() {
        return this.fila;
    }
    public int getColumna() {
        return this.col;
    }

    public Unidad getOcupante() {
        return this.ocupante;
    }

    public boolean estaOcupada() {
        return ocupante != null;
    }

    public void ocupar(Unidad unidad){
        // *A* fijarse excepcion de si esta ocupada,si la unidad es nula, si es intransitable, etc
        // ...
        ocupante = unidad;
    }

    public void desocupar(){
        ocupante = null;
    }

}
