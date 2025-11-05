package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;
import org.modelo.unidades.Unidad;

public class Agua extends Casilla {

    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("AG", Agua::new);
    }

    public Agua(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return false; }
    @Override
    public int getCostoMovimiento() { return 1; }
    @Override
    public String getTipoTerreno() {
        return "Agua";
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
