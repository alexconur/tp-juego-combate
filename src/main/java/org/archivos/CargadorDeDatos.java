// src/main/java/org/archivos/CargadorDeDatos.java
package org.archivos;

import java.util.List;

import org.modelo.tablero.Tablero;
import org.modelo.unidades.Unidad; 

public final class CargadorDeDatos {
    private final EjercitoLoader ejercitoLoader;

    public CargadorDeDatos(EjercitoLoader ejercitoLoader) {
        this.ejercitoLoader = ejercitoLoader;
    }

    public Tablero cargarMapa(String mapaResourcePath) {
        return MapaLoader.cargar(mapaResourcePath);
    }

    public List<Unidad> cargarEjercito(String ejercitoResourcePath) {
        return this.ejercitoLoader.cargar(ejercitoResourcePath);
    }
}