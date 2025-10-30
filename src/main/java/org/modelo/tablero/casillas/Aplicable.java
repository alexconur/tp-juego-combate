package org.modelo.tablero.casillas;

import org.modelo.unidades.Unidad;

public interface Aplicable {
    void aplicarEfectoAlEntrar(Unidad unidad);
    void aplicarEfectoFinDeTurno(Unidad unidad);
    void aplicarEfectoDePosicion(Unidad unidad);
    void revertirEfectoDePosicion(Unidad unidad);
}
