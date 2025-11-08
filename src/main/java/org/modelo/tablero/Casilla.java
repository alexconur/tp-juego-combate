package org.modelo.tablero;

import org.modelo.tablero.casillas.EfectoAlEntrar;
import org.modelo.tablero.casillas.EfectoDePosicion;
import org.modelo.tablero.casillas.EfectoFinDeTurno;
import org.modelo.tablero.excepciones.CasillaIntransitableException;
import org.modelo.tablero.excepciones.CasillaOcupadaException;
import org.modelo.unidades.Unidad;

public abstract class Casilla {
    // Atributos
    private int fila;
    private int columna;
    private Unidad ocupante;

    private final EfectoAlEntrar efectoAlEntrar;
    private final EfectoFinDeTurno efectoFinDeTurno;
    private final EfectoDePosicion efectoDePosicion;

    // Constructor
    protected Casilla(int fila, int columna, EfectoAlEntrar efectoAlEntrar,
                      EfectoFinDeTurno efectoFinDeTurno,
                      EfectoDePosicion efectoDePosicion) {
        this.fila = fila;
        this.columna = columna;
        this.ocupante = null;
        this.efectoAlEntrar = efectoAlEntrar;
        this.efectoFinDeTurno = efectoFinDeTurno;
        this.efectoDePosicion = efectoDePosicion;
    }

    // --- Métodos de Estado y Posición ---

    public int getFila() { return this.fila; }
    public int getColumna() { return this.columna; }
    public Unidad getOcupante() { return this.ocupante; }
    public boolean estaOcupada() { return ocupante != null; }
    public String getTipoTerreno() { return this.getClass().getSimpleName(); }

    // --- Métodos de Ocupación ---
    // Coloca una unidad en esta casilla.
        // Lanza excepciones si la casilla no es un destino válido.
        // Actualiza la referencia interna de la unidad.
    public void ocupar(Unidad unidad) throws CasillaOcupadaException, CasillaIntransitableException {
        if (estaOcupada()) {
            throw new CasillaOcupadaException("La casilla (" + fila + "," + columna + ") ya está ocupada.");
        }
        if (!esTransitable()) {
            throw new CasillaIntransitableException("La casilla (" + fila + "," + columna + ") es intransitable.");
        }
        
        this.ocupante = unidad;
        
        // La casilla le informa a la unidad dónde está.
        if (unidad != null) {
            unidad.setCasillaActual(this); 
        }
    }

    // Quita la unidad de esta casilla. Actualiza la referencia de la casilla
    public Unidad desocupar() {
        Unidad unidadQueSeVa = this.ocupante;
        // La unidad ya no está en ninguna casilla (hasta que ocupe otra)
        if (unidadQueSeVa != null) {
            unidadQueSeVa.setCasillaActual(null);
        }
        
        this.ocupante = null;
        return unidadQueSeVa;
    }

    public void notificarFinDeTurno() {
        if (ocupante != null && efectoFinDeTurno != null) {
            efectoFinDeTurno.aplicar(ocupante, this);
        }
    }

    public void aplicarEfectoDePosicion(Unidad unidad) {
        if (efectoDePosicion != null) {
            efectoDePosicion.aplicar(unidad);
        }
    }

    public boolean permiteEmboscada() { return false; }

    // --- Métodos de Efectos ---
    // Define si una unidad puede transitar la casilla o no.
    public abstract boolean esTransitable();

    public abstract String getCodigoColorVista();
}
