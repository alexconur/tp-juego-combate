package org.modelo.equipamiento.estrategias;

import org.modelo.unidades.Unidad;

public interface EstrategiaBaculo {
    void aplicarEfecto(Unidad portador, Unidad objetivo, int poder);
}