package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;

public class AreaContaminada extends Casilla implements Aplicable {
    private static final int HP_DISMINUIDO = 5;  // Valor hipotético

    public AreaContaminada(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
    @Override
    public int getCostoMovimiento() { return 1; }
    @Override
    public void aplicarEfectoAlEntrar(Unidad unidad) {}
    @Override
    public String getTipoTerreno() {
        return "Area Contaminada";
    }
    @Override
    public void aplicarEfectoFinDeTurno(Unidad unidad) {
        unidad.recibirDanio(HP_DISMINUIDO);
        System.out.println(unidad.getNombre() + " recibió daño " + HP_DISMINUIDO + " en Area Contaminada.");
    }
    @Override
    public void aplicarEfectoDePosicion(Unidad unidad) {}
    @Override
    public void revertirEfectoDePosicion(Unidad unidad) {} 
}
