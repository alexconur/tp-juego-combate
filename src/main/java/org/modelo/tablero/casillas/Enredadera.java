package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;

public class Enredadera extends Casilla {
    public Enredadera(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return false; }
    @Override
    public int getCostoMovimiento() { return 1; }
    @Override
    public String getTipoTerreno() {
        return "Enredadera";
    }
    @Override
    public void aplicarEfectoAlEntrar(Unidad unidad) {}

    @Override
    public void aplicarEfectoFinDeTurno(Unidad unidad) {}
    @Override
    public void aplicarEfectoDePosicion(Unidad unidad) {}
    @Override
    public void revertirEfectoDePosicion(Unidad unidad) {} 
}
