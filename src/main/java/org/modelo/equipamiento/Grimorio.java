package org.modelo.equipamiento;
import org.modelo.unidades.Unidad;

public class Grimorio extends Equipamiento {

    public Grimorio(String nombre, int bonusMagia, int rango, int usos){
        super(nombre, 0, bonusMagia, rango,usos);
    }

    @Override
    public void accionar(Unidad portador, Unidad objetivo) {
        if (estaRoto()) return;

        int daño = portador.getMgcTotal() - objetivo.getMgcTotal();
        objetivo.recibirDanio(Math.max(0, daño));

        this.usar();
    }

    @Override
    public boolean esMagico(){ return true; }

    @Override
    public boolean esOfensivo(){ return true; }

}


