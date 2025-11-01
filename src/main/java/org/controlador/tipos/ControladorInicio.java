// src/main/java/org/controlador/tipos/ControladorInicio.java
package org.controlador.tipos;

import java.util.List;

import org.archivos.CargadorDeDatos;
import org.archivos.CsvReader;
import org.modelo.Juego;
import org.modelo.tablero.Tablero;
import org.vista.tipos.VistaInicio;

public class ControladorInicio {

    private final Juego juego;
    private final VistaInicio vInicio;

    // Se guardan por si los necesita otro controlador
    private String mapaPath, ejercitoPath, arsenalPath;
    private int lordFila, lordColumna;

    public ControladorInicio(Juego juego, VistaInicio vInicio) {
        this.juego = juego;
        this.vInicio = vInicio;
    }

    public void ejecutar() {
        // 1) Pantalla de bienvenida
        vInicio.mostrar();

        // 2) Seleccionar archivos (desde resources)
        VistaInicio.Selecciones sel = vInicio.seleccionarArchivos();
        mapaPath     = sel.getMapaPath();
        ejercitoPath = sel.getEjercitoPath();
        arsenalPath  = sel.getArsenalPath();

        // 3) Leer solo dimensiones del mapa para validar el rango de ubicación
        List<List<String>> celdas = CsvReader.readResource(mapaPath);
        if (celdas == null || celdas.isEmpty()) {
            throw new IllegalArgumentException("El mapa está vacío: " + mapaPath);
        }
        int filas = celdas.size();
        int columnas = celdas.get(0).size();
        for (int i = 1; i < filas; i++) {
            if (celdas.get(i).size() != columnas) {
                throw new IllegalArgumentException(
                    "Mapa no rectangular (fila " + i + " tiene " + celdas.get(i).size() +
                    " columnas; esperado " + columnas + ")"
                );
            }
        }

        // 4) Pedir ubicación del Lord dentro del rango [0..filas-1] x [0..columnas-1]
        VistaInicio.Ubicacion ubi = vInicio.pedirUbicacionLord(filas, columnas);
        lordFila = ubi.getFila();
        lordColumna = ubi.getColumna();

        // 5) Cargar el mapa en el modelo y mostrarlo (la Vista se encarga de la salida)
        CargadorDeDatos cargador = new CargadorDeDatos();
        Tablero tablero = cargador.cargarMapa(mapaPath);
        juego.reemplazarTablero(tablero);
        vInicio.mostrarTablero(tablero);

        // Próximo paso: validar que la casilla elegida sea transitable y ubicar al Lord ahí,
        //  luego cargar arsenal/ejército con los otros paths y continuar el flujo de juego
    }
    
    public String getMapaPath()     { return mapaPath; }
    public String getEjercitoPath() { return ejercitoPath; }
    public String getArsenalPath()  { return arsenalPath; }
    public int getLordFila()        { return lordFila; }
    public int getLordColumna()     { return lordColumna; }
}
