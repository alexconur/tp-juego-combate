package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;
import org.modelo.unidades.Unidad;

public class Bosque extends Casilla implements Aplicable {
    private static final int BONUS_ATAQUE = 5;  // Valor hipotético
    private static final int BONUS_MAGIA = 5;  // Valor hipotético

    // *A* sé que static se ve feo pero no hay otra forma por ahora de no romper ocp
    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("BO", Bosque::new);
    }
    
    public Bosque(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
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
        unidad.aplicarBonusTemporal("ATK", BONUS_ATAQUE);
        unidad.aplicarBonusTemporal("MGC", BONUS_MAGIA);
    }
    @Override
    public void revertirEfectoDePosicion(Unidad unidad) {} 
}
