// src/main/java/org/archivos/CargadorDeDatos.java
package org.archivos;

import org.modelo.tablero.Tablero;

public final class CargadorDeDatos {
    public Tablero cargarMapa(String mapaResourcePath) {
        return MapaLoader.cargar(mapaResourcePath);
    }
    // más adelante: cargarArsenal y cargarEjercito. usar EquipamientoLoader y EjercitoLoader seguro
}