// src/main/java/org/archivos/CargadorDeDatos.java
package org.archivos;

import java.util.List;

import org.modelo.tablero.Tablero;
import org.modelo.unidades.Unidad; 

public final class CargadorDeDatos {
    private final EjercitoLoader ejercitoLoader;
    private final MapaLoader mapaLoader;

    public CargadorDeDatos(EjercitoLoader ejercitoLoader, MapaLoader mapaLoader) {
        this.ejercitoLoader = ejercitoLoader;
        this.mapaLoader = mapaLoader;
    }

    public Tablero cargarMapa(String mapaResourcePath) {
        return this.mapaLoader.cargar(mapaResourcePath);
    }

    public List<Unidad> cargarEjercito(String ejercitoResourcePath) {
        return this.ejercitoLoader.cargar(ejercitoResourcePath);
    }
}