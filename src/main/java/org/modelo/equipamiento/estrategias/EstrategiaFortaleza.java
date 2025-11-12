package org.modelo.equipamiento.estrategias;

import org.modelo.unidades.Unidad;

public class EstrategiaFortaleza implements EstrategiaBaculo {
    @Override
    public void aplicarEfecto(Unidad portador, Unidad objetivo, int poder) {
        objetivo.aplicarBonusDefTemporal(poder);
        System.out.println(objetivo.getNombre() + " recibe bonus de fortaleza!");
    }
}