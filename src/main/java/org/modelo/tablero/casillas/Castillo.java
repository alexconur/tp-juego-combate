package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class Castillo extends Casilla {
    public Castillo(int fila, int columna) {
        super(fila, columna);
    }

    public boolean esTransitable() {
        return true;
    }

    public void efectos(){

    }
}
