package org.modelo.equipamiento;

public class Grimorio extends Equipamiento {

    public Grimorio(String nombre, int bonusMagia, int rango, int usos){
        super(nombre, 0, bonusMagia, rango,usos);
    }

    public boolean esMagico(){
        return true;
    }

    public boolean esOfensivo(){
        return true;
    }

}


