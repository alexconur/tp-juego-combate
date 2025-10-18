package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class Enredadera extends Casilla {
    public Enredadera(int fila, int columna) {
        super(fila, columna);
    }

    public boolean esTransitable() {
        return false;
    }

    public void efectos(){

    }
}
