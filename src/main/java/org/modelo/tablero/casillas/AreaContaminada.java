package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class AreaContaminada extends Casilla {
    public AreaContaminada(int fila, int columna) {
        super(fila, columna);
    }

    public boolean esTransitable() {
        return true;
    }

    public void efectos(){

    }
}
