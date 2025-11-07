package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;
import org.modelo.unidades.Unidad;

public class Llanura extends Casilla {
    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("LL", Llanura::new);
    }

    public Llanura(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
    @Override
    public String getTipoTerreno() {
        return "Llanura";
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
