package org.modelo.tablero;

import java.util.List;

import org.modelo.tablero.excepciones.CasillaIntransitableException;
import org.modelo.tablero.excepciones.CasillaOcupadaException;
import org.modelo.tablero.casillas.*;
import org.modelo.unidades.Unidad;
import org.modelo.unidades.Bando;

public abstract class Casilla {

    private int fila;
    private int columna;
    private Unidad ocupante;
    private final EfectoFinDeTurno efectoFinDeTurno;
    private final EfectoDePosicion efectoDePosicion;
    private final EfectoAlEntrar efectoAlEntrar;

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

    public abstract boolean esTransitable();
    public abstract String getCodigoColorVista();
    public abstract String descripcionEfecto();

    public int getFila() { return this.fila; }
    public int getColumna() { return this.columna; }
    public Unidad getOcupante() { return this.ocupante; }
    public String getTipoTerreno() { return this.getClass().getSimpleName(); }
    
    public boolean estaOcupada() { return ocupante != null; }
    public boolean permiteEmboscada() { return false; }

    public void ocupar(Unidad unidad) throws CasillaOcupadaException, CasillaIntransitableException {
        if (estaOcupada()) {
            throw new CasillaOcupadaException("La casilla (" + fila + "," + columna + ") ya está ocupada.");
        }
        if (!esTransitable()) {
            throw new CasillaIntransitableException("La casilla (" + fila + "," + columna + ") es intransitable.");
        }
        
        this.ocupante = unidad;
        
        if (unidad != null) {
            unidad.setCasillaActual(this); 
        }
    }

    public Unidad desocupar() {
        Unidad unidadQueSeVa = this.ocupante;

        if (unidadQueSeVa != null) {
            unidadQueSeVa.setCasillaActual(null);
        }
        
        this.ocupante = null;
        return unidadQueSeVa;
    }

    public void notificarFinDeTurno(Bando bandoActual) {
        if (ocupante != null && efectoFinDeTurno != null) {
            efectoFinDeTurno.aplicar(ocupante, this, bandoActual);
        }
    }

    public void aplicarEfectoDePosicion(Unidad unidad) {
        if (efectoDePosicion != null) {
            efectoDePosicion.aplicar(unidad);
        }
    }

    public void aplicarEfectoAlEntrar(Unidad unidad) {
        if (efectoAlEntrar != null) {
            efectoAlEntrar.aplicar(unidad);
        }
    }

    public static List<InfoCasilla> obtenerInformacion() {
        return List.of(
            new InfoCasilla(new Bosque(0, 0)),
            new InfoCasilla(new Llanura(0, 0)),
            new InfoCasilla(new Pantano(0, 0)),
            new InfoCasilla(new Castillo(0, 0)),
            new InfoCasilla(new Agua(0, 0)),
            new InfoCasilla(new AreaContaminada(0, 0)),
            new InfoCasilla(new Acantilado(0, 0)),
            new InfoCasilla(new Enredadera(0, 0))
        );
    }
}
