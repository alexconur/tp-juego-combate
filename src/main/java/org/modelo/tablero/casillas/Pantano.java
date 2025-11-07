package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;
import org.modelo.unidades.Unidad;

public class Pantano extends Casilla implements Aplicable {

    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("PA", Pantano::new);
    }

    public Pantano(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
    @Override
    public String getTipoTerreno() {
        return "Pantano";
    }
    @Override
    public void aplicarEfectoAlEntrar(Unidad unidad) {
        unidad.setMovimientoRestante(1);
        System.out.println(unidad.getNombre() + " se atasca en el pantano!");

    }
    @Override
    public void aplicarEfectoFinDeTurno(Unidad unidad) {}
    @Override
    public void aplicarEfectoDePosicion(Unidad unidad) {
        // *X*: No entendi la condicion de esto en el PDf
    }
    @Override
    public void revertirEfectoDePosicion(Unidad unidad) {} 
}
