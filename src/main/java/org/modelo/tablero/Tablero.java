package org.modelo.tablero;

import org.modelo.unidades.Unidad;
import org.modelo.tablero.excepciones.CasillaOcupadaException;
import org.modelo.tablero.excepciones.CasillaIntransitableException;

public class Tablero {
    // Atributos
    private int filas;
    private int columnas;
    private Casilla[][] casillas;

    // Constructor
    private Tablero(int filas, int columnas){
        this.filas=filas;
        this.columnas=columnas;
        this.casillas = new Casilla[filas][columnas];
    }

    // Devuelve la casilla en la posicion dada. Incluye chequeo de limites
    public Casilla getCasilla(int fila, int columna){
        if (!posicionValida(fila,columna)){
            return null;
        }
        return casillas[fila][columna];
    }

    // Coloca un tipo de casilla (Terreno) en el tablero
        // *X*: Esto lo usa el "CargadorDeMapas". (a implementar)
    public void setCasilla(int fila, int columna, Casilla nuevaCasilla){
        if(!posicionValida(fila,columna)){
            return;
        }
        casillas[fila][columna] = nuevaCasilla;
    }

    // Ayuda a verificar si una coordenada esta dentro del tablero
    public boolean posicionValida(int fila, int columna) {
        return fila >= 0 && fila < this.filas && columna >= 0 && columna < this.columnas;
    }

    // Mueve una unidad de una casilla a otra. 
    public void moverUnidad(Unidad unidad, int nuevaFila, int nuevaCol) 
            throws CasillaOcupadaException, CasillaIntransitableException {
        
        if (unidad == null) return;
        
        Casilla destino = getCasilla(nuevaFila, nuevaCol);
        if (destino == null) {
            throw new CasillaIntransitableException("Posición de destino fuera del tablero.");
        }
        // 1. Desocupar la casilla actual (si tiene una)
        Casilla origen = unidad.getCasillaActual();
        if (origen != null) {
            origen.desocupar();
        }
        // 2. Ocupar la nueva casilla
        destino.ocupar(unidad);
    }
}