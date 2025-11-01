package org.archivos;

import java.util.List;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;
import org.modelo.tablero.Tablero;

public final class MapaLoader {

    private MapaLoader() {}

    public static Tablero cargar(String resourcePath) {
        List<List<String>> t = CsvReader.readResource(resourcePath);
        if (t.isEmpty()) throw new IllegalArgumentException("Mapa vacío: " + resourcePath);

        int filas = t.size();
        int cols  = t.get(0).size();
        for (int i = 1; i < filas; i++) {
            if (t.get(i).size() != cols) {
                throw new IllegalArgumentException("Mapa no rectangular en fila " + i);
            }
        }

        Tablero tablero = new Tablero(filas, cols);
        FabricaCasillas fabrica = new FabricaCasillas();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                String token = t.get(i).get(j);
                String tipo = normalizar(token);
                Casilla c = fabrica.crearCasilla(tipo, i, j);
                tablero.setCasilla(i, j, c);
            }
        }
        return tablero;
    }

    // *A* Adapta códigos cortos del CSV a los tipos que espera FabricaCasillas
    private static String normalizar(String raw) {
        String s = raw == null ? "" : raw.trim().toUpperCase();
        switch (s) {
            case "LL": return "LLANURA";
            case "BO": return "BOSQUE";
            case "PA": return "PANTANO";
            case "FO": case "F": case "CASTILLO": case "FUERTE": return "CASTILLO";
            case "AR": case "AREA": case "AREA_CONT": case "AREACONT": return "AREACONT";
            case "AG": return "AGUA";
            case "EN": return "ENREDADERA";
            case "AC": return "ACANTILADO";
            default:    return s; // si ya vino como LLANURA/BOSQUE/etc.
        }
    }
}
