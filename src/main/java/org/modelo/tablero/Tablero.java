package org.modelo.tablero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

    public int getFilas(){
        return filas;
    }

    public int getColumnas(){
        return columnas;
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
    }

    // Devuelve las casillas vecinas (en las 8 direcciones posibles)
    // que están dentro de los límites del tablero.
    private List<Casilla> obtenerVecinos(Casilla c) {
        List<Casilla> vecinos = new ArrayList<>();
        int fila = c.getFila();
        int col = c.getColumna();

        // Recorremos las 8 direcciones (horizontal, vertical y diagonales)
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                // Ignorar la casilla original
                if (df == 0 && dc == 0) continue;

                int nuevaFila = fila + df;
                int nuevaColumna = col + dc;

                // Solo agregamos si está dentro de los límites del tablero
                if (posicionValida(nuevaFila, nuevaColumna)) {
                    vecinos.add(getCasilla(nuevaFila, nuevaColumna));
                }
            }
        }

        return vecinos;
    }

    public List<Casilla> obtenerCasillasAlcanzables(Unidad unidad) {
        List<Casilla> alcanzables = new ArrayList<>();
        if (unidad == null || unidad.getCasillaActual() == null) return alcanzables;
        
        Casilla origen = unidad.getCasillaActual();
        
        if ("Pantano".equals(origen.getTipoTerreno())) {
            for (Casilla vecino : obtenerVecinos(origen)) {
                if (vecino != null && vecino.esTransitable() && !vecino.estaOcupada()) {
                    alcanzables.add(vecino);
                }
            }
            return alcanzables;
        }
        
        int maxMovimiento = unidad.getMovimientoRestante();

        // Estructuras BFS
        Queue<Casilla> cola = new LinkedList<>();
        Map<Casilla, Integer> distancias = new HashMap<>();

        cola.add(origen);
        distancias.put(origen, 0);

        while (!cola.isEmpty()) {
            Casilla actual = cola.poll();
            int distanciaActual = distancias.get(actual);

            // Agregar a lista si no es la de origen
            if (!actual.equals(origen)) {
                alcanzables.add(actual);
            }

            // Si no podemos seguir avanzando, skip
            if (distanciaActual >= maxMovimiento) continue;

            // Explorar vecinos en 8 direcciones
            for (Casilla vecino : obtenerVecinos(actual)) {
                if (vecino == null) continue;

                // Verificamos si es transitable y no ocupada
                if (!vecino.esTransitable() || vecino.estaOcupada()) continue;

                int nuevoCosto = distanciaActual + 1;
                if (nuevoCosto <= maxMovimiento && (!distancias.containsKey(vecino) || nuevoCosto < distancias.get(vecino))) {
                    distancias.put(vecino, nuevoCosto);
                    cola.add(vecino);
                }
            }
        }

        return alcanzables;
    }
}