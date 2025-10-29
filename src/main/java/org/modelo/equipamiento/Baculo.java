package org.modelo.equipamiento;

import org.modelo.unidades.Unidad;

public class Baculo extends Equipamiento {
    // Atributos
    private TipoBaculo tipo;
    private int poderCuracion;

    // Constructor
    public Baculo(String nombre, TipoBaculo tipo, int bonusMagia, int usos, int poderCuracion) {
        // falta
        super(nombre,0,bonusMagia,Integer.MAX_VALUE, usos); // chequear lo Integer xq sino me daba error.
        this.tipo=tipo;
        this.poderCuracion = poderCuracion;
    }

    @Override
    public void accionar(Unidad portador, Unidad objetivo) {
        if (estaRoto() || portador.getBando() != objetivo.getBando()) return; // No curar enemigos

        // *X*: esto hace la acción de curar segun el tipo de Baculo, pero usa switch (a mejorar)
        switch (this.tipo) {
            case CURACION:
                // Restaura moderadamente el HP
                objetivo.recibirCuracion(this.poderCuracion);
                break;
            case SANACION:
                // Restaura todo el HP 
                objetivo.recibirCuracion(objetivo.getMaxHp());
                break;
            case FORTALEZA:
                // Aumenta defensas 
                objetivo.aplicarBonusTemporal("DEF", this.poderCuracion);
                System.out.println(objetivo.getNombre() + " recibe bonus de fortaleza!");
                break;
        }
        this.usar(); // Gasta un uso
    }

    public TipoBaculo getTipoBaculo(){ return this.tipo; }

    @Override   
    public boolean esMagico() { return true; }

    @Override
    public boolean esOfensivo(){ return false; }
}
