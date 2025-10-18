package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class Llanura extends Casilla {
    public Llanura(int fila, int columna) {
        super(fila, columna);
    }

    public boolean esTransitable() {
        return true;
    }

    public void efectos(){

    }
}
