package org.modelo.tablero;

public class Tablero {
    // Super hipotetico, el tablero debe cargarse desde un archivo (como tropas)
    private static Tablero tablero;
    private Casilla[][] casilla;

    private Tablero(int n, int m){
        casilla = new Casilla[n][m];
    }

    // Singleton para el tablero
    public static Tablero crearTablero(int n, int m){
        if(tablero == null){
            Tablero.tablero = new Tablero(n, m);
            return Tablero.tablero;
        }
        return tablero;
    }

    public Casilla getCasilla(int fila, int columna){
        return casilla[fila][columna];
    }

    public void setCasilla(int fila, int columna, Casilla nuevaCasilla){
        casilla[fila][columna] = nuevaCasilla;
    }
    
    // Metodos ideas
    // public boolean posicionValida();
    // 

}