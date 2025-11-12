package org.modelo.equipamiento;

import org.modelo.equipamiento.estrategias.EstrategiaBaculo;
import org.modelo.unidades.Unidad;

public class Baculo extends Equipamiento {
    private EstrategiaBaculo estrategia;
    private int poderCuracion;

    public Baculo(String nombre, EstrategiaBaculo estrategia, int bonusMagia, int usos, int poderCuracion) {
        super(nombre,0,bonusMagia,Integer.MAX_VALUE, usos);
        this.estrategia = estrategia;
        this.poderCuracion = poderCuracion;
    }

    @Override
    public void accionar(Unidad portador, Unidad objetivo) {
        if (estaRoto() || portador.getBando() != objetivo.getBando()) return;

        this.estrategia.aplicarEfecto(portador, objetivo, this.poderCuracion);
        this.usar();
    }

    @Override   
    public boolean esMagico() { return true; }

    @Override
    public boolean esOfensivo(){ return false; }
}
