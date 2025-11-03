// src/main/java/org/controlador/tipos/ControladorInicio.java
package org.controlador.tipos;

import java.util.List;

import org.archivos.CargadorDeDatos;
import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaInicio;

public class ControladorInicio {

    private final Juego juego;
    private final VistaInicio vInicio;

    // Se guardan por si los necesita otro controlador
    private String mapaPath, ejercitoPath;

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

        CargadorDeDatos cargador = new CargadorDeDatos();

        // 3) Cargar el mapa en el modelo
        Tablero tablero = cargador.cargarMapa(mapaPath);
        juego.reemplazarTablero(tablero);

        // 4) Cargar Ejército (ambos bandos desde el mismo archivo)
        List<Unidad> todasLasUnidades = cargador.cargarEjercito(ejercitoPath);
        juego.setEjercitos(todasLasUnidades);

        // 5) Mostrar tablero y pedir ubicación del Lord (Jugador 1)
        vInicio.mostrarTablero(tablero, juego.getBandoActual());

        Bando bandoJ1 = juego.getBandoActual();
        System.out.println("Jugador 1 (" + bandoJ1 + "), posiciona a tu Lord.");
        
        Unidad lordJ1 = juego.getLordDeReserva(bandoJ1);
        if (lordJ1 == null) {
            throw new IllegalStateException("No se encontró un Lord para el " + bandoJ1);
        }

        // Pedir ubicación hasta que sea válida
        while(true) {
            VistaInicio.Ubicacion ubi = vInicio.pedirUbicacionLord(tablero.getFilas(), tablero.getColumnas());
            Casilla c = tablero.getCasilla(ubi.getFila(), ubi.getColumna());
            
            if (c.esTransitable() && !c.estaOcupada()) {
                juego.desplegarUnidad(lordJ1, ubi.getFila(), ubi.getColumna());
                System.out.println("✔ Lord " + lordJ1.getNombre() + " desplegado en (" + ubi.getFila() + "," + ubi.getColumna() + ").");
                break;
            }
            vInicio.mostrarTablero(tablero, juego.getBandoActual());
            System.out.println("¡Ubicación inválida! La casilla no es transitable o está ocupada.");
        }

        // 7) Posicionar Lord del Jugador 2 (interactivo, igual que J1)
        Bando bandoJ2 = (bandoJ1 == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;
        Unidad lordJ2 = juego.getLordDeReserva(bandoJ2);
        
        System.out.println("Jugador 2 (" + bandoJ2 + "), posiciona a tu Lord.");
        // Mostrar tablero antes de la selección del J2
        if (lordJ2 == null) {
            throw new IllegalStateException("No se encontró un Lord para el " + bandoJ2);
        }

        vInicio.mostrarTablero(tablero, bandoJ2);
        
        while (true) {
            VistaInicio.Ubicacion ubi2 = vInicio.pedirUbicacionLord(tablero.getFilas(), tablero.getColumnas());
            Casilla c2 = tablero.getCasilla(ubi2.getFila(), ubi2.getColumna());
            if (c2.esTransitable() && !c2.estaOcupada()) {
                juego.desplegarUnidad(lordJ2, ubi2.getFila(), ubi2.getColumna());
                System.out.println("✔ Lord " + lordJ2.getNombre() + " desplegado en (" + ubi2.getFila() + "," + ubi2.getColumna() + ").");
                break;
            }
            System.out.println("¡Ubicación inválida! La casilla no es transitable o está ocupada.");
            vInicio.mostrarTablero(tablero, bandoJ2);
        }


        // 8) Mostrar tablero actualizado
        vInicio.mostrarTablero(tablero, juego.getBandoActual());
        
        // 9) El flujo continúa en ControladorPrincipal
    }
 
    // *X* no se usa mas, por las dudas lo comento
    // public String getMapaPath()     { return mapaPath; }
    // public String getEjercitoPath() { return ejercitoPath; }
    // public String getArsenalPath()  { return arsenalPath; }
    // public int getLordFila()        { return lordFila; }
    // public int getLordColumna()     { return lordColumna; }
}
