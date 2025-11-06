package org.controlador.tipos;

import java.util.List;

import org.archivos.CargadorDeDatos;
import org.controlador.Controlador;
import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.tipos.VistaInicio;

public class ControladorInicio implements Controlador {

    private final Juego juego;
    private final VistaInicio vInicio;

    private String mapaPath, ejercitoPath;

    public ControladorInicio(Juego juego, VistaInicio vInicio) {
        this.juego = juego;
        this.vInicio = vInicio;
    }

    // *X* ACA HAY CODIGO REPETIDO!!
    public void ejecutar() {
        vInicio.mostrar();

        // 2) Seleccionar archivos
        VistaInicio.Selecciones sel = vInicio.seleccionarArchivos();
        mapaPath = sel.getMapaPath();
        ejercitoPath = sel.getEjercitoPath();

        CargadorDeDatos cargador = new CargadorDeDatos();

        // 3) Cargar el mapa en el modelo
        Tablero tablero = cargador.cargarMapa(mapaPath);
        juego.reemplazarTablero(tablero);

        // 4) Cargar Ejército
        List<Unidad> todasLasUnidades = cargador.cargarEjercito(ejercitoPath);
        juego.setEjercitos(todasLasUnidades);

        // 5) Posicionar Lord (Jugador 1)
        Bando bandoJ1 = juego.getBandoActual();        
        Unidad lordJ1 = juego.getLordDeReserva(bandoJ1);

        if (lordJ1 == null) { throw new IllegalStateException("No se encontró un Lord para el " + bandoJ1); }

        while(true) {
            // Mostramos el tablero ANTES de pedir la ubicación
            vInicio.mostrarTablero(tablero, bandoJ1);
            VistaInicio.Ubicacion ubi = vInicio.pedirUbicacionLord(bandoJ1, tablero.getFilas(), tablero.getColumnas());
            
            Casilla c = tablero.getCasilla(ubi.getFila(), ubi.getColumna());
            
            if (c != null && c.esTransitable() && !c.estaOcupada()) {
                juego.desplegarUnidad(lordJ1, ubi.getFila(), ubi.getColumna());
                System.out.println("✔ Lord " + lordJ1.getNombre() + " desplegado en (" + ubi.getFila() + "," + ubi.getColumna() + ").");
                break;
            }
            System.out.println(Colores.WARNING + "¡Ubicación inválida! La casilla no es transitable o está ocupada." + Colores.RESET);
        }

        // 6) Posicionar Lord del Jugador 2
        Bando bandoJ2 = (bandoJ1 == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;
        Unidad lordJ2 = juego.getLordDeReserva(bandoJ2);
                
        if (lordJ2 == null) {
            throw new IllegalStateException("No se encontró un Lord para el " + bandoJ2);
        }
        
        // *A* acá hay un while true feo jajaj
        while (true) {
            // Mostramos el tablero ANTES de pedir la ubicación
            vInicio.mostrarTablero(tablero, bandoJ2);
            VistaInicio.Ubicacion ubi2 = vInicio.pedirUbicacionLord(bandoJ2, tablero.getFilas(), tablero.getColumnas());
            
            Casilla c2 = tablero.getCasilla(ubi2.getFila(), ubi2.getColumna());
            if (c2 != null && c2.esTransitable() && !c2.estaOcupada()) {
                juego.desplegarUnidad(lordJ2, ubi2.getFila(), ubi2.getColumna());
                System.out.println("✔ Lord " + lordJ2.getNombre() + " desplegado en (" + ubi2.getFila() + "," + ubi2.getColumna() + ").");
                break;
            }
            System.out.println(Colores.WARNING + "¡Ubicación inválida! La casilla no es transitable o está ocupada." + Colores.RESET);
        }        
    }
}