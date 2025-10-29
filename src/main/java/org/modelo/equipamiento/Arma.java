package org.modelo.equipamiento;
import org.modelo.unidades.Unidad;

// Arma física (Espada, Hacha, Lanza)
public class Arma extends Equipamiento {
    private TipoArma tipo;

    public Arma(String nombre, TipoArma tipo, int bonusAtaque, int rango, int usos){
        super(nombre, bonusAtaque, 0, rango, usos);
        this.tipo=tipo;
    }

    @Override
    public void accionar(Unidad portador, Unidad objetivo) {
        if (estaRoto()) return;

        // Cálculo de daño físico: ATK (atacante) - DEF (atacado)
        int daño = portador.getAtkTotal() - objetivo.getDef();

        // Aplicar el daño (asegurando que no sea negativo)
        objetivo.recibirDanio(Math.max(0, daño));

        this.usar(); // Gastar un uso
    }

    public TipoArma getTipo() { return this.tipo; }

    @Override
    public boolean esMagico(){ return false; }

    @Override
    public boolean esOfensivo() { return true; }







}