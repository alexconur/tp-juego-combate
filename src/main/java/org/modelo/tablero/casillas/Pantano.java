package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;

public class Pantano extends Casilla implements Aplicable {
    public Pantano(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public boolean esTransitable() { return true; }
    @Override
    public int getCostoMovimiento() { return 1; }
    @Override
    public void aplicarEfectoAlEntrar(Unidad unidad) {
        // Implementar lógica que reduce el movimiento de la unidad.
        // Por ejemplo: unidad.setMovimientoRestante(1);
        // (Esto requiere agregar lógica de movimiento a la Unidad)
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
