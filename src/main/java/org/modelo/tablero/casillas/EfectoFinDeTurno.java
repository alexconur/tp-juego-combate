package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;

public interface EfectoFinDeTurno {
    void aplicar(Unidad unidad, Casilla casilla);
}
