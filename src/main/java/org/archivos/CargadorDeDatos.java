// src/main/java/org/archivos/CargadorDeDatos.java
package org.archivos;

import java.util.List;

import org.modelo.tablero.Tablero;
import org.modelo.unidades.Unidad; 

public final class CargadorDeDatos {
    public Tablero cargarMapa(String mapaResourcePath) {
        return MapaLoader.cargar(mapaResourcePath);
    }

    // Cargar Ejército
    public List<Unidad> cargarEjercito(String ejercitoResourcePath) {
        return EjercitoLoader.cargar(ejercitoResourcePath);
    }
}