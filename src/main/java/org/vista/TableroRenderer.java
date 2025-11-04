// src/main/java/org/vista/util/TableroAsciiRenderer.java
package org.vista;

import org.modelo.tablero.Tablero;
import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;
import org.modelo.unidades.Bando;

public class TableroRenderer {

    public static String render(Tablero tablero, Bando bandoActual) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== TERRENOS ===\n");
        sb.append(renderTerrenos(tablero));
        sb.append("\n=== UNIDADES ===\n");
        sb.append(renderUnidades(tablero, bandoActual));

        return sb.toString();
    }

    // Render del tipo de terreno
    private static String renderTerrenos(Tablero tablero) {
        StringBuilder sb = new StringBuilder();

        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int col = 0; col < tablero.getColumnas(); col++) {
                Casilla casilla = tablero.getCasilla(fila, col);
                sb.append(simboloTerreno(casilla)).append("  ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    // Render de las unidades (propias ocultas, visibles o vacías)
    private static String renderUnidades(Tablero tablero, Bando bandoActual) {
        StringBuilder sb = new StringBuilder();

        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int col = 0; col < tablero.getColumnas(); col++) {
                Casilla casilla = tablero.getCasilla(fila, col);
                Unidad u = casilla.getOcupante();

                if (u != null && u.estaVivo()) {
                    // Si pertenece al jugador actual
                    if (u.getBando() == bandoActual) {
                        if (u.isOculto()) {
                            sb.append(".  "); // unidad propia oculta
                        } else {
                            String inicial = u.getNombre().substring(0, 1).toUpperCase();
                            sb.append(inicial + "  "); // unidad propia visible
                        }
                    } else {
                        // Enemigo → nunca mostrar si está oculto
                        if (u.isOculto()) {
                            sb.append("-  "); // invisible
                        } else {
                            String inicial = u.getNombre().substring(0, 1).toUpperCase();
                            sb.append(inicial + "  "); // enemigo visible
                        }
                    }
                } else {
                    sb.append("-  "); // Casilla vacía
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    // Símbolos para cada tipo de terreno
    private static String simboloTerreno(Casilla casilla) {
        String tipo = casilla.getClass().getSimpleName();

        switch (tipo) {
            case "Bosque": return "BO";
            case "Llanura": return "LL";
            case "Pantano": return "PA";
            case "Castillo": return "FO";
            case "Enredadera": return "EN";
            case "AreaContaminada": return "AR";
            case "Agua": return "AG";
            case "Acantilado": return "AC";
            default: return "-";
        }
    }
}