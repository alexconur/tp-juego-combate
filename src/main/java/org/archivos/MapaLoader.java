package org.archivos;

import java.util.List;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;
import org.modelo.tablero.Tablero;

public final class MapaLoader {

    private final FabricaCasillas fabricaCasillas;

    public MapaLoader(FabricaCasillas fabricaCasillas) {
        this.fabricaCasillas = fabricaCasillas;
    }

    public Tablero cargar(String resourcePath) {
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

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                String token = t.get(i).get(j);
                Casilla c = this.fabricaCasillas.crearCasilla(token, i, j);
                tablero.setCasilla(i, j, c);
            }
        }
        return tablero;
    }
}
