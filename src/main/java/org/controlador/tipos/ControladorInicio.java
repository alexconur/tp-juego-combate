package org.controlador.tipos;

import java.util.List;
import java.util.Scanner;

import org.archivos.CargadorDeDatos;
import org.archivos.EjercitoLoader;
import org.archivos.MapaLoader;
import org.archivos.factories.EquipamientoDefault;
import org.archivos.factories.EquipamientoFactory;
import org.archivos.factories.EstrategiaDefault;
import org.archivos.factories.EstrategiaFactory;
import org.archivos.factories.UnidadDefault;
import org.archivos.factories.UnidadFactory;
import org.controlador.Controlador;
import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.tipos.VistaInicio;


public class ControladorInicio implements Controlador {
    private final Juego juego;
    private final VistaInicio vInicio;
    private final Scanner scanner = new Scanner(System.in);

    private String mapaPath, ejercitoPath;

    public ControladorInicio(Juego juego, VistaInicio vInicio) {
        this.juego = juego;
        this.vInicio = vInicio;
    }

    public void ejecutar() {
        vInicio.mostrar();

        VistaInicio.Selecciones sel = vInicio.seleccionarArchivos();
        mapaPath = sel.getMapaPath();
        ejercitoPath = sel.getEjercitoPath();

        EstrategiaFactory estrategiaFactory = new EstrategiaDefault();
        EquipamientoFactory equipamientoFactory = new EquipamientoDefault(estrategiaFactory);
        UnidadFactory unidadFactory = new UnidadDefault();

        EjercitoLoader ejercitoLoader = new EjercitoLoader(unidadFactory, equipamientoFactory);

        FabricaCasillas fabricaCasillas = FabricaCasillas.getInstancia();
        MapaLoader mapaLoader = new MapaLoader(fabricaCasillas);

        CargadorDeDatos cargador = new CargadorDeDatos(ejercitoLoader, mapaLoader);

        Tablero tablero = cargador.cargarMapa(mapaPath);
        juego.reemplazarTablero(tablero);

        List<Unidad> todasLasUnidades = cargador.cargarEjercito(ejercitoPath);
        juego.setEjercitos(todasLasUnidades);

        Bando bandoJ1 = juego.getBandoActual();
        Bando bandoJ2 = (bandoJ1 == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;

        posicionarLord(bandoJ1, tablero);
        posicionarLord(bandoJ2, tablero);

        faseDeDespliegue(bandoJ1, tablero);
        faseDeDespliegue(bandoJ2, tablero);
        
        System.out.println("\n¡Todo listo para la batalla!");
        // (Pausa para que el jugador lea antes de que ControladorTurno limpie la pantalla)
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private void posicionarLord(Bando bando, Tablero tablero) {
        Unidad lord = juego.getLordDeReserva(bando);
                
        if (lord == null) {
            throw new IllegalStateException("No se encontró un Lord para el " + bando);
        }
        
        boolean posicionadoConExito = false;
        while (!posicionadoConExito) {
            // Mostramos el tablero ANTES de pedir la ubicación
            vInicio.mostrarTablero(tablero, bando);
            VistaInicio.Ubicacion ubi = vInicio.pedirUbicacionLord(bando, tablero.getFilas(), tablero.getColumnas());
            
            Casilla c = tablero.getCasilla(ubi.getFila(), ubi.getColumna());
            
            // Validamos que la casilla sea transitable y no esté ocupada
            if (c != null && c.esTransitable() && !c.estaOcupada()) {
                juego.desplegarUnidad(lord, ubi.getFila(), ubi.getColumna());
                System.out.println("✔ Lord " + lord.getNombre() + " desplegado en (" + ubi.getFila() + "," + ubi.getColumna() + ").");
                posicionadoConExito = true;
                break; // Salimos del bucle si el despliegue es exitoso
            }
            
            // Si llegamos aquí, la ubicación no fue válida
            System.out.println(Colores.WARNING + "¡Ubicación inválida! La casilla no es transitable o está ocupada." + Colores.RESET);
        }
    }

    private void faseDeDespliegue(Bando bando, Tablero tablero) {
            System.out.println("\n--- FASE DE DESPLIEGUE: " + bando + " ---");
            System.out.println("\n--Las unidades de tu bando solo pueden desplegarse en casillas adyacentes a su Lord--");
            
            boolean despliegueCompleto = false;
            while (!despliegueCompleto) {
                List<Unidad> enReserva = juego.getUnidadesEnReserva(bando);
                if (enReserva.isEmpty()) {
                    System.out.println("No quedan unidades en la reserva.");
                    despliegueCompleto = true;
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
                VistaInicio.Ubicacion ubi = vInicio.pedirUbicacionUnidad(unidadADesplegar, bando, tablero.getFilas(), tablero.getColumnas());
                
                // 3. Intentar desplegar (Juego.desplegarUnidad ahora valida adyacencia)
                boolean desplegada = juego.desplegarUnidad(unidadADesplegar, ubi.getFila(), ubi.getColumna());
    
                if (desplegada) {
                    // 4. Preguntar por modo Oculto
                    // (Se comprueba el tipo de terreno sin usar instanceof)
                    Casilla c = tablero.getCasilla(ubi.getFila(), ubi.getColumna());
                    if (c != null && c.permiteEmboscada()) {
                        
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