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

    private int filas;
    private int columnas;
    private Casilla[][] casillas;

    public Tablero(int filas, int columnas){
        this.filas=filas;
        this.columnas=columnas;
        this.casillas = new Casilla[filas][columnas];
    }

    public int getFilas(){ return filas; }
    public int getColumnas(){ return columnas; }

    public Casilla getCasilla(int fila, int columna){
        if (!posicionValida(fila,columna)){
            return null;
        }
        return casillas[fila][columna];
    }

    public void setCasilla(int fila, int columna, Casilla nuevaCasilla){
        if(!posicionValida(fila,columna)){
            return;
        }
        casillas[fila][columna] = nuevaCasilla;
    }

    public boolean posicionValida(int fila, int columna) {
        return fila >= 0 && fila < this.filas && columna >= 0 && columna < this.columnas;
    }

    public void moverUnidad(Unidad unidad, int nuevaFila, int nuevaColumna)
            throws CasillaOcupadaException, CasillaIntransitableException {

        if (unidad == null) return;

        if (!posicionValida(nuevaFila, nuevaColumna)) {
            throw new CasillaIntransitableException("Movimiento fuera del tablero");
        }

        Casilla destino = getCasilla(nuevaFila, nuevaColumna);

        if (!destino.esTransitable()) {
            throw new CasillaIntransitableException("Casilla intransitable");
        }

        if (destino.estaOcupada()) {
            throw new CasillaOcupadaException("Casilla ya ocupada");
        }

        Casilla origen = unidad.getCasillaActual();
        if (origen != null) {
            origen.desocupar();
            unidad.resetearBonusTemporales();
        }

        destino.ocupar(unidad);
        unidad.setCasillaActual(destino);
        destino.aplicarEfectoAlEntrar(unidad);     
        destino.aplicarEfectoDePosicion(unidad);   
    }

    private List<Casilla> obtenerVecinos(Casilla c) {
        List<Casilla> vecinos = new ArrayList<>();
        int fila = c.getFila();
        int col = c.getColumna();

        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (df == 0 && dc == 0) continue;

                int nuevaFila = fila + df;
                int nuevaColumna = col + dc;

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
        int maxMovimiento = unidad.getMovimientoRestante();
        Queue<Casilla> cola = new LinkedList<>();
        Map<Casilla, Integer> distancias = new HashMap<>();

        cola.add(origen);
        distancias.put(origen, 0);

        while (!cola.isEmpty()) {
            Casilla actual = cola.poll();
            int distanciaActual = distancias.get(actual);

            if (!actual.equals(origen)) {
                alcanzables.add(actual);
            }

            if (distanciaActual >= maxMovimiento) continue;

            for (Casilla vecino : obtenerVecinos(actual)) {
                if (vecino == null) continue;
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

    public boolean esAdyacente(Casilla c1, Casilla c2, int radio){
        if (c1 == null || c2 == null) return false;
        int distFila = Math.abs(c1.getFila() - c2.getFila());
        int distCol = Math.abs(c1.getColumna() - c2.getColumna());

        return Math.max(distFila, distCol) <= radio;
    }
}