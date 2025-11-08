package org.modelo.equipamiento.estrategias;

import org.modelo.unidades.Unidad;

public class EstrategiaFortaleza implements EstrategiaBaculo {
    @Override
    public void aplicarEfecto(Unidad portador, Unidad objetivo, int poder) {
        // Usamos el método refactorizado de Unidad (ver punto 3)
        objetivo.aplicarBonusTemporal("DEF",poder);
        System.out.println(objetivo.getNombre() + " recibe bonus de fortaleza!");
    }
}