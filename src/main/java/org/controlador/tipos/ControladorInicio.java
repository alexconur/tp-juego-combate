package org.controlador.tipos;

import java.util.List;
import java.util.Scanner;

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
    private final Scanner sc = new Scanner(System.in);

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
        // FASE DE DESPLIEGUE (El bandoJ1 es el que empieza)
        faseDeDespliegue(bandoJ1, tablero);
        faseDeDespliegue(bandoJ2, tablero);
        
        System.out.println("\n¡Todo listo para la batalla!");
        // (Pausa para que el jugador lea antes de que ControladorTurno limpie la pantalla)
        System.out.println("Presione Enter para continuar...");
        sc.nextLine();
    }

    private void faseDeDespliegue(Bando bando, Tablero tablero) {
            System.out.println("\n--- FASE DE DESPLIEGUE: " + bando + " ---");
            System.out.println("\n--Las unidades de tu bando solo pueden desplegarse en casillas adyacentes a su Lord--");
            
            while (true) {
                List<Unidad> enReserva = juego.getUnidadesEnReserva(bando);
                if (enReserva.isEmpty()) {
                    System.out.println("No quedan unidades en la reserva.");
                    break;
                }
    
                // 1. Mostrar tablero y pedir unidad
                vInicio.mostrarTablero(tablero, bando);
                Unidad unidadADesplegar = vInicio.seleccionarUnidadDeReserva(bando, enReserva);
    
                if (unidadADesplegar == null) {
                    System.out.println("Terminando fase de despliegue para " + bando);
                    break; // El jugador eligió [0] Terminar
                }
    
                // 2. Pedir ubicación
                // (Usamos pedirUbicacionLord, pero es solo para pedir fila/columna)
                VistaInicio.Ubicacion ubi = vInicio.pedirUbicacionLord(bando, tablero.getFilas(), tablero.getColumnas());
                
                // 3. Intentar desplegar (Juego.desplegarUnidad ahora valida adyacencia)
                boolean desplegada = juego.desplegarUnidad(unidadADesplegar, ubi.getFila(), ubi.getColumna());
    
                if (desplegada) {
                    // 4. Preguntar por modo Oculto
                    // (Se comprueba el tipo de terreno sin usar instanceof)
                    Casilla c = tablero.getCasilla(ubi.getFila(), ubi.getColumna());
                    if (c != null && c.getTipoTerreno().equals("Bosque")) {
                        
                        boolean ocultar = vInicio.pedirConfirmacion("¿Desplegar esta unidad en modo oculto?");
                        if (ocultar) {
                            unidadADesplegar.setOculto(true);
                            System.out.println("🕵️ " + unidadADesplegar.getNombre() + " se ha desplegado en modo oculto.");
                        }
                    }

                    System.out.println("✔ " + unidadADesplegar.getNombre() + " desplegado en (" + ubi.getFila() + "," + ubi.getColumna() + ").");
                } else {
                    // El despliegue falló (Juego.java ya imprimió el error)
                    System.out.println(Colores.WARNING + "Despliegue fallido. Intente en otra casilla." + Colores.RESET);
                    // La unidad no se quita de la reserva, el bucle principal se repite
                }
            }
        }
}