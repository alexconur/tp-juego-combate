package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class Bosque extends Casilla {
    public Bosque(int fila, int columna) {
        super(fila, columna);
    }

    public boolean esTransitable() {
        return true;
    }

    public void efectos(){

    }
}
