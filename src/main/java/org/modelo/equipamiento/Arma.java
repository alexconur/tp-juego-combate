package org.modelo.equipamiento;


public class Arma extends Equipamiento {
    private TipoArma tipo;

    public Arma(String nombre, TipoArma tipo, int bonusAtaque, int rango, int usos){
        super(nombre, bonusAtaque, 0, rango, usos);
        this.tipo=tipo;
    }

    public TipoArma getTipo() {
        return this.tipo;
    }

    public boolean esMagico(){
        return false;
    }

    public boolean esOfensivo() {
        return true;
    }







}