// src/main/java/org/vista/util/TableroAsciiRenderer.java
package org.vista;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class TableroRenderer {

    // Renderiza el tablero de juego en una sola vista unificada.
    public static String render(Tablero tablero, Bando bandoActual) {
        return render(tablero, bandoActual, null);
    }

    public static String render(Tablero tablero, Bando bandoActual, List<Casilla> casillasResaltadas) {
        StringBuilder sb = new StringBuilder();

        // Convertir la lista a un Set para búsquedas
        final Set<Casilla> setResaltadas = (casillasResaltadas == null)
                ? Collections.emptySet()
                : new HashSet<>(casillasResaltadas);

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
                String bg; // Obtenemos el color del fondo actual
                // Si la casilla está en el set de resaltadas, usamos el nuevo color
                if (setResaltadas.contains(casilla)) {
                    bg = Colores.TERRENO_ALCANZABLE_BG;
                } else {
                    bg = getSimboloTerrenoBG(casilla);
                }
                // obtenemos el caracter y color de frente para la unidad. Luego combinamos y reseteamos
                String fg = getSimboloUnidadFG(casilla, bandoActual);
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
                return Colores.TERRENO_ENREDADERA_BG;
            case "AreaContaminada": 
                return Colores.TERRENO_PELIGROSO_BG;
            case "Agua":
                return Colores.TERRENO_AGUA_BG;
            case "Acantilado": 
                return Colores.TERRENO_ACANTILADO_BG;
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