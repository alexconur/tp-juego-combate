package org.modelo.equipamiento;

import org.modelo.equipamiento.estrategias.EstrategiaBaculo;
import org.modelo.unidades.Unidad;

public class Baculo extends Equipamiento {
    // Atributos
    private EstrategiaBaculo estrategia;
    private int poderCuracion;

    // Constructor
    public Baculo(String nombre, EstrategiaBaculo estrategia, int bonusMagia, int usos, int poderCuracion) {
        // falta
        super(nombre,0,bonusMagia,Integer.MAX_VALUE, usos);
        this.estrategia = estrategia;
        this.poderCuracion = poderCuracion;
    }

    @Override
    public void accionar(Unidad portador, Unidad objetivo) {
        if (estaRoto() || portador.getBando() != objetivo.getBando()) return; // No curar enemigos

        this.estrategia.aplicarEfecto(portador, objetivo, this.poderCuracion);
        this.usar(); // Gasta un uso
    }

    @Override   
    public boolean esMagico() { return true; }

    @Override
    public boolean esOfensivo(){ return false; }
}
