package org.modelo.tablero;

import org.modelo.tablero.excepciones.CasillaIntransitableException;
import org.modelo.tablero.excepciones.CasillaOcupadaException;
import org.modelo.unidades.Unidad;

public class Tablero {
    // Atributos
    private int filas;
    private int columnas;
    private Casilla[][] casillas;

    // Constructor
    public Tablero(int filas, int columnas){
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
    public void moverUnidad(Unidad unidad, int nuevaFila, int nuevaColumna)
        throws CasillaOcupadaException, CasillaIntransitableException {

    if (unidad == null) return;

    if (!posicionValida(nuevaFila, nuevaColumna)) {
        throw new CasillaIntransitableException("Movimiento fuera del tablero");
    }

    Casilla destino = getCasilla(nuevaFila, nuevaColumna);

    // 1. Verificar si la casilla permite el movimiento
    if (!destino.esTransitable()) {
        throw new CasillaIntransitableException("Casilla intransitable");
    }

    if (destino.estaOcupada()) {
        throw new CasillaOcupadaException("Casilla ya ocupada");
    }

    // 2. Liberar la casilla actual
    Casilla origen = unidad.getCasillaActual();
    if (origen != null) origen.desocupar();

    // 3. Mover la unidad a la nueva posición
    destino.ocupar(unidad);
    unidad.setCasillaActual(destino);

    // 4. Aplicar efectos del terreno si los hay
    if (destino instanceof org.modelo.tablero.casillas.Aplicable a) {
        a.aplicarEfectoAlEntrar(unidad);
    }

    System.out.println(unidad.getNombre() + " se movió a (" + nuevaFila + "," + nuevaColumna + ").");
}
}