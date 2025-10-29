package org.modelo.equipamiento;

import org.modelo.unidades.Unidad;

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

    // --- Getters ---
    public String getNombre(){ return this.nombre; }
    public int getRango(){ return this.rango; }
    public int getUsosRestantes(){ return this.usosRestantes; }

    public int getBonusAtaque(){ return this.bonusAtaque; }
    public int getBonusMagia(){ return this.bonusMagia; }


    // --- Métodos de Estado ---
    // Reduce los usos del equipamiento. Se llama después de que accionar() se ejecute.
    public void usar(){
        if (usosRestantes > 0){
            usosRestantes-= 1;  
        }

    }
    // Verifica si el equipamiento esta roto.
    public boolean estaRoto() {
        if (usosRestantes <=0) {
            return true;
        }
        return false;
    }


    // --- Métodos Abstractos (Patron Strategy) ---
        
    // Se realiza la accion principal del equipamiento (atacar o curar)
        // portador: la unidad que usa el equipamiento
        // objetivo: la unidad que recibe la accion.
    public abstract void accionar(Unidad portador, Unidad objetivo);

    // Metodos para indicar si el equipamiento es magico u ofensivo.
    public abstract boolean esMagico();
    public abstract boolean esOfensivo();

}