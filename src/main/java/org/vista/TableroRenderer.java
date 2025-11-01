// src/main/java/org/vista/util/TableroAsciiRenderer.java
package org.vista;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.tablero.casillas.Acantilado;
import org.modelo.tablero.casillas.Agua;
import org.modelo.tablero.casillas.AreaContaminada;
import org.modelo.tablero.casillas.Bosque;
import org.modelo.tablero.casillas.Castillo;
import org.modelo.tablero.casillas.Enredadera;
import org.modelo.tablero.casillas.Llanura;
import org.modelo.tablero.casillas.Pantano;

public final class TableroRenderer {
    private TableroRenderer() {}

    public static String render(Tablero t) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t.getFilas(); i++) {
            for (int j = 0; j < t.getColumnas(); j++) {
                sb.append(simbolo(t.getCasilla(i, j))).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static char simbolo(Casilla c) {
        // *A* recontra viola OCP
        if (c instanceof Llanura)         return '.';
        if (c instanceof Bosque)          return 'B';
        if (c instanceof Pantano)         return 'P';
        if (c instanceof Castillo)        return 'F';
        if (c instanceof AreaContaminada) return 'X';
        if (c instanceof Agua
         || c instanceof Enredadera
         || c instanceof Acantilado)     return '#';
        return '?';
    }
}