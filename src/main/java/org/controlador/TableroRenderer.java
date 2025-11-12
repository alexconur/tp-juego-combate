package org.controlador;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;

public class TableroRenderer {

    public static String render(Tablero tablero, Bando bandoActual) {
        return render(tablero, bandoActual, null);
    }

    public static String render(Tablero tablero, Bando bandoActual, List<Casilla> casillasResaltadas) {
        StringBuilder sb = new StringBuilder();

        final Set<Casilla> setResaltadas = (casillasResaltadas == null)
                ? Collections.emptySet()
                : new HashSet<>(casillasResaltadas);

        sb.append("   ");
        for(int col= 0; col< tablero.getColumnas(); col++){
            sb.append(String.format("%-3s", col));
        }
        sb.append("\n");

        for(int fila=0; fila<tablero.getFilas(); fila++){
            sb.append(String.format("%-2s ", fila));

            for(int col=0; col<tablero.getColumnas(); col++){
                Casilla casilla = tablero.getCasilla(fila, col);                
                String bg;

                if (setResaltadas.contains(casilla)) {
                    bg = Colores.TERRENO_ALCANZABLE_BG;
                } else {
                    bg = getSimboloTerrenoBG(casilla, bandoActual);
                }
                String fg = getSimboloUnidadFG(casilla, bandoActual);
                sb.append(bg).append(fg).append(Colores.RESET);
            }
            sb.append("\n");
        }
        sb.append(Colores.RESET);
        return sb.toString();
    }

    private static String getSimboloTerrenoBG(Casilla casilla, Bando bandoActual) {
        if (casilla == null) return Colores.TERRENO_DEFAULT_BG;

        Unidad u = casilla.getOcupante();
        if (u != null && u.isOculto() && u.getBando() != bandoActual) {
            return Colores.TERRENO_ENREDADERA_BG;
        }
        return casilla.getCodigoColorVista();
    }

    private static String getSimboloUnidadFG(Casilla casilla, Bando bandoActual) {
        if (casilla == null) return " ? ";
        
        Unidad u = casilla.getOcupante();

        if (u == null || !u.estaVivo()) {
            return Colores.VACIO_U + " · ";
        }

        String inicial = u.getNombre().substring(0, 1).toUpperCase();
        boolean oculto = u.isOculto();
        boolean esAliado = (u.getBando() == bandoActual);

        String colorNormal = (u.getBando() == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
        String colorOculto = (u.getBando() == Bando.REINO_DRUIDA) ? Colores.DRUIDA_OCULTO : Colores.NIGROMANTICO_OCULTO;

        if (oculto) {
            return esAliado ? (colorOculto + " " + inicial + " ") : (Colores.VACIO_U + " · ");
        }
        return colorNormal + " " + inicial + " ";
    }
}