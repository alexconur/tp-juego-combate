package org.modelo.equipamiento;

import org.modelo.unidades.Unidad;

public abstract class Equipamiento {

    protected String nombre;
    protected int bonusAtaque;
    protected int bonusMagia;
    protected int usosRestantes;
    protected int rango;

    public Equipamiento(String nombre,int bonusAtaque,int bonusMagia, int rango, int usosRestantes){
        this.nombre=nombre;
        this.bonusAtaque=bonusAtaque;
        this.bonusMagia=bonusMagia;
        this.usosRestantes=usosRestantes;
        this.rango=rango;
    }

    public abstract void accionar(Unidad portador, Unidad objetivo);
    public abstract boolean esMagico();
    public abstract boolean esOfensivo();

    public String getNombre(){ return this.nombre; }
    public int getRango(){ return this.rango; }
    public int getUsosRestantes(){ return this.usosRestantes; }
    public int getBonusAtaque(){ return this.bonusAtaque; }
    public int getBonusMagia(){ return this.bonusMagia; }

    public void usar(){
        if (usosRestantes > 0){
            usosRestantes-= 1;  
        }

    }

    public boolean estaRoto() {
        if (usosRestantes <=0) {
            return true;
        }
        return false;
    }
}