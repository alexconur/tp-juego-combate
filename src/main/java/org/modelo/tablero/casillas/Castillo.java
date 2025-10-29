package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;

public class Castillo extends Casilla {
    private static final int CURACION_FUERTE = 10; // Valor hipotético
    private static final int BONUS_DEFENSA = 5;  // Valor hipotético
    public Castillo(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
    @Override
    public int getCostoMovimiento() { return 1; } // costo normal
    
    // *X*: No hace nada al entrar, pero estariamos violando ISP
    @Override
    public void aplicarEfectoAlEntrar(Unidad unidad) {}

    @Override
    public void aplicarEfectoFinDeTurno(Unidad unidad) {
        // 1. Recupera HP
        unidad.recibirCuracion(CURACION_FUERTE);
        
        // 2. Aumenta la defensa
        // (agregar lógica en Unidad para manejar bonus temporales)
        unidad.recibirCuracion(CURACION_FUERTE);
        unidad.aplicarBonusTemporal("DEF", BONUS_DEFENSA);
        System.out.println(unidad.getNombre() + " se cura " + CURACION_FUERTE + " HP en el fuerte.");
    }
    @Override
    public void aplicarEfectoDePosicion(Unidad unidad) {} // El Castillo no da bonus pasivos, solo al final del turno
    @Override
    public void revertirEfectoDePosicion(Unidad unidad) {} // No hay efectos pasivos que revertir
}
