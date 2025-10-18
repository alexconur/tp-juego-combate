package org.modelo.equipamiento;

public class Baculo extends Equipamiento {
    private TipoBaculo tipo;

    // Constructor
    public Baculo(String nombre, TipoBaculo tipo, int bonusMagia, int usos) {
        // falta
        super(nombre,0,bonusMagia,Integer.MAX_VALUE, usos); // chequear lo Integer xq sino me daba error.
        this.tipo=tipo;
    }

    public TipoBaculo getTipoBaculo(){
        return this.tipo;
    }

    public boolean esMagico() {
        return true;
    }

    public boolean esOfensivo(){
        return false;
    }
}
