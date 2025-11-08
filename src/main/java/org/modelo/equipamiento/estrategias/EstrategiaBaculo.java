package org.modelo.equipamiento.estrategias;

import org.modelo.unidades.Unidad;

public interface EstrategiaBaculo {
    // Aplica el efecto del báculo a una unidad objetivo.
    void aplicarEfecto(Unidad portador, Unidad objetivo, int poder);
}