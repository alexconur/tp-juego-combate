package org.modelo.tablero;

import org.modelo.unidades.Unidad;

import org.modelo.tablero.excepciones.CasillaOcupadaException;
import org.modelo.tablero.excepciones.CasillaIntransitableException;

public abstract class Casilla {
    // Atributos
    private int fila;
    private int col;
    private Unidad ocupante;

    // Constructor
    protected Casilla(int fila, int columna) {
        this.fila = fila;
        this.col = columna;
        this.ocupante = null;
    }


    // --- Métodos de Estado y Posición ---

    public int getFila() { return this.fila; }
    public int getColumna() { return this.col; }
    public Unidad getOcupante() { return this.ocupante; }
    public boolean estaOcupada() { return ocupante != null; }

    // --- Métodos de Ocupación ---
    // Coloca una unidad en esta casilla.
        // Lanza excepciones si la casilla no es un destino válido.
        // Actualiza la referencia interna de la unidad.
    public void ocupar(Unidad unidad) throws CasillaOcupadaException, CasillaIntransitableException {
        if (estaOcupada()) {
            throw new CasillaOcupadaException("La casilla (" + fila + "," + col + ") ya está ocupada.");
        }
        if (!esTransitable()) {
            throw new CasillaIntransitableException("La casilla (" + fila + "," + col + ") es intransitable.");
        }
        
        this.ocupante = unidad;
        
        // La casilla le informa a la unidad dónde está.
        if (unidad != null) {
            unidad.setCasillaActual(this); 
            this.aplicarEfectoAlEntrar(unidad); // Aplicar efectos INMEDIATOS al entrar
        }
    }

    // Quita la unidad de esta casilla. Actualiza la referencia de la casilla
    public Unidad desocupar() {
        Unidad unidadQueSeVa = this.ocupante;
        // La unidad ya no está en ninguna casilla (hasta que ocupe otra)
        if (unidadQueSeVa != null) {
            unidadQueSeVa.setCasillaActual(null);
            // Revertir efectos de posición (si es necesario)
            this.revertirEfectoDePosicion(unidadQueSeVa);
        }
        
        this.ocupante = null;
        return unidadQueSeVa;
    }

    // --- Métodos de Efectos ---
    // Define si una unidad puede transitar la casilla o no.
    public abstract boolean esTransitable();

    // Define el costo al moverse en esta casilla
    public abstract int getCostoMovimiento();

    // Aplica efectos que ocurren cuando una unidad ENTRA a la casilla.
    public abstract void aplicarEfectoAlEntrar(Unidad unidad);

    // Aplica efectos que ocurren al FINALIZAR EL TURNO en esta casilla.
    public abstract void aplicarEfectoFinDeTurno(Unidad unidad);
    
    // Modifica las estadísticas de una unidad MIENTRAS ESTÉ parada aquí.
        // Este método es llamado por la Unidad en sus getters (getAtkTotal()).
    public abstract void aplicarEfectoDePosicion(Unidad unidad);

    // Revierte los efectos de posición cuando la unidad se va.
    public abstract void revertirEfectoDePosicion(Unidad unidad);
}
