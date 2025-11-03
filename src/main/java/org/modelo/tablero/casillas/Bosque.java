package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;

public class Bosque extends Casilla implements Aplicable {
    private static final int BONUS_DEFENSA = 5;  // Valor hipotético
    private static final int BONUS_MAGIA = 5;  // Valor hipotético


    public Bosque(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
    @Override
    public int getCostoMovimiento() { return 1; }
    @Override
    public String getTipoTerreno() {
        return "Bosque";
    }
    @Override
    public void aplicarEfectoAlEntrar(Unidad unidad) {}
    @Override
    public void aplicarEfectoFinDeTurno(Unidad unidad) {}
    @Override
    public void aplicarEfectoDePosicion(Unidad unidad) {
        // Aumento BONUS
        unidad.aplicarBonusTemporal("ATK", BONUS_DEFENSA);
        unidad.aplicarBonusTemporal("MGC", BONUS_MAGIA);
    }
    @Override
    public void revertirEfectoDePosicion(Unidad unidad) {} 
}
