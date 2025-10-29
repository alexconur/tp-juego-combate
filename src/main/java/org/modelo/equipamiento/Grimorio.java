package org.modelo.equipamiento;
import org.modelo.unidades.Unidad;

// Arma mágica (Grimorio)
public class Grimorio extends Equipamiento {

    public Grimorio(String nombre, int bonusMagia, int rango, int usos){
        super(nombre, 0, bonusMagia, rango,usos);
    }

    @Override
    public void accionar(Unidad portador, Unidad objetivo) {
        if (estaRoto()) return;

        // Cálculo de daño mágico: MGC (atacante) - MGC (atacado)
        int daño = portador.getMgcTotal() - objetivo.getMgcTotal();

        // Aplicar el daño (asegurando que no sea negativo)
        objetivo.recibirDanio(Math.max(0, daño));

        this.usar(); // Gastar un uso
    }

    @Override
    public boolean esMagico(){ return true; }

    @Override
    public boolean esOfensivo(){ return true; }

}


