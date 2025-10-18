package org.modelo.equipamiento;

public abstract class Equipamiento {

    // Atributos
    protected String nombre;
    protected int bonusAtaque;
    protected int bonusMagia;
    protected int usosRestantes;
    protected int rango;


    // Constructor
    public Equipamiento(String nombre,int bonusAtaque,int bonusMagia, int rango, int usosRestantes){
        this.nombre=nombre;
        this.bonusAtaque=bonusAtaque;
        this.bonusMagia=bonusMagia;
        this.usosRestantes=usosRestantes;
        this.rango=rango;
    }

    // getters
    public String getNombre(){
        return this.nombre;
    }

    public int getRango(){
        return this.rango;
    }

    public int getUsosRestantes(){
        return this.usosRestantes;
    }

    // Metodos
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


    // Metodos abstractos
    public abstract boolean esMagico();    
    public abstract boolean esOfensivo();
}