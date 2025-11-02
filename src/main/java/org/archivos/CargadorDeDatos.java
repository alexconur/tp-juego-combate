// src/main/java/org/archivos/CargadorDeDatos.java
package org.archivos;

import org.modelo.tablero.Tablero;
import org.modelo.equipamiento.Equipamiento;
import org.modelo.unidades.Unidad;
import java.util.List; 
import java.util.Map; 

public final class CargadorDeDatos {
    public Tablero cargarMapa(String mapaResourcePath) {
        return MapaLoader.cargar(mapaResourcePath);
    }

    // Cargar Arsenal
    public Map<String, Equipamiento> cargarArsenal(String arsenalResourcePath) {
        return EquipamientoLoader.cargarPorNombre(arsenalResourcePath);
    }
    
    // Cargar Ejército
    public List<Unidad> cargarEjercito(String ejercitoResourcePath, Map<String, Equipamiento> arsenal) {
        return EjercitoLoader.cargar(ejercitoResourcePath, arsenal);
    }
}