// src/main/java/org/vista/util/TableroAsciiRenderer.java
package org.vista;

import org.modelo.tablero.Tablero;
import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;
import org.modelo.unidades.Bando;

public class TableroRenderer {

    // Renderiza el tablero de juego en una sola vista unificada.
    public static String render(Tablero tablero, Bando bandoActual) {
        StringBuilder sb = new StringBuilder();

        // Cabeceras COLUMNAS
        sb.append("   ");
        for(int col= 0; col< tablero.getColumnas(); col++){
            sb.append(String.format("%-3s", col));
        }
        sb.append("\n");

        // Cabeceras FILAS
        for(int fila=0; fila<tablero.getFilas(); fila++){
            sb.append(String.format("%-2s ", fila));

            for(int col=0; col<tablero.getColumnas(); col++){
                Casilla casilla = tablero.getCasilla(fila, col);
                // Obtenemos el color rde fondo para el terreno
                String bg = getSimboloTerrenoBG(casilla);

                // obtenemos el caracter y color de frente para la unidad
                String fg = getSimboloUnidadFG(casilla, bandoActual);

                // combinamos y resetear
                sb.append(bg).append(fg).append(Colores.RESET);
            }
            sb.append("\n");
        }
        sb.append(Colores.RESET);
        return sb.toString();
    }

        // Devuelve el código ANSI de color de FONDO para un tipo de casilla.
    private static String getSimboloTerrenoBG(Casilla casilla) {
        if (casilla == null) return Colores.TERRENO_DEFAULT_BG;
        String tipo = casilla.getClass().getSimpleName();

        switch (tipo) {
            case "Bosque": 
                return Colores.TERRENO_BOSQUE_BG;
            case "Llanura": 
                return Colores.TERRENO_LLANURA_BG;
            case "Pantano": 
                return Colores.TERRENO_PANTANO_BG;
            case "Castillo": 
                return Colores.TERRENO_CASTILLO_BG;
            case "Enredadera": 
                return Colores.TERRENO_INTRANSITABLE_BG;
            case "AreaContaminada": 
                return Colores.TERRENO_PELIGROSO_BG;
            case "Agua":
                return Colores.TERRENO_AGUA_BG;
            case "Acantilado": 
                return Colores.TERRENO_INTRANSITABLE_BG;
            default: 
                return Colores.TERRENO_DEFAULT_BG;
        }
    }

        // Devuelve el código ANSI de FRENTE + el carácter de la unidad.
        // Cada celda tiene 3 caracteres de ancho.
    private static String getSimboloUnidadFG(Casilla casilla, Bando bandoActual) {
        if (casilla == null) return " ? ";
        
        Unidad u = casilla.getOcupante();

        if (u == null || !u.estaVivo()) {
            return Colores.VACIO_U + " · "; // Casilla vacía
        }

        String inicial = u.getNombre().substring(0, 1).toUpperCase();
        
        if (u.getBando() == bandoActual) {
            // Es Aliado
            if (u.isOculto()) {
                return Colores.ALIADO_OCULTO + " " + inicial + " ";
            } else {
                return Colores.ALIADO + " " + inicial + " ";
            }
        } else {
            // Es Enemigo
            if (u.isOculto()) {
                return Colores.VACIO_U + " · "; // Enemigo oculto se ve como vacío
            } else {
                return Colores.ENEMIGO + " " + inicial + " ";
            }
        }
    }
}