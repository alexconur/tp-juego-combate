package org.modelo.equipamiento.estrategias;

import org.modelo.unidades.Unidad;

public class EstrategiaSanacion implements EstrategiaBaculo {
    @Override
    public void aplicarEfecto(Unidad portador, Unidad objetivo, int poder) {
        objetivo.recibirCuracion(objetivo.getMaxHp()); 
    }
}