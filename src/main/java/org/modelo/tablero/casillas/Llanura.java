package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;

public class Llanura extends Casilla {
    public Llanura(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
    @Override
    public int getCostoMovimiento() { return 1; }
    @Override
    public void aplicarEfectoAlEntrar(Unidad unidad) {}
    @Override
    public void aplicarEfectoFinDeTurno(Unidad unidad) {}
    @Override
    public void aplicarEfectoDePosicion(Unidad unidad) {}
    @Override
    public void revertirEfectoDePosicion(Unidad unidad) {} 
}
