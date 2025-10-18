package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class Agua extends Casilla {
    public Agua(int fila, int columna) {
        super(fila, columna);
    }

    public boolean esTransitable() {
        return false;
    }

    public void efectos(){

    }
}
