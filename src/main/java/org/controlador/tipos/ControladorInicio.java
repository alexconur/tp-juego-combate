package org.controlador.tipos;

import java.util.ArrayList;
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
import org.modelo.tablero.RegistroDeCasillas;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.TableroRenderer;
import org.vista.tipos.SeleccionesInicio;
import org.vista.tipos.UbicacionInicio;
import org.vista.tipos.VistaInicio;


public class ControladorInicio implements Controlador {
    private final Juego juego;
    private final VistaInicio vInicio;
    private final CargadorDeDatos cargador;
    private final Scanner scanner = new Scanner(System.in);

    private String mapaPath, ejercitoPath;

    public ControladorInicio(Juego juego, VistaInicio vInicio) {
        this.juego = juego;
        this.vInicio = vInicio;

        EstrategiaFactory estrategiaFactory = new EstrategiaDefault();
        EquipamientoFactory equipamientoFactory = new EquipamientoDefault(estrategiaFactory);
        UnidadFactory unidadFactory = new UnidadDefault();
        EjercitoLoader ejercitoLoader = new EjercitoLoader(unidadFactory, equipamientoFactory);

        FabricaCasillas fabricaCasillas = new FabricaCasillas();
        RegistroDeCasillas.registrarTodo(fabricaCasillas);

        MapaLoader mapaLoader = new MapaLoader(fabricaCasillas);

        this.cargador = new CargadorDeDatos(ejercitoLoader, mapaLoader);
    }

    public void ejecutar() {
        vInicio.mostrar();

        SeleccionesInicio sel = vInicio.seleccionarArchivos();
        mapaPath = sel.getMapaPath();
        ejercitoPath = sel.getEjercitoPath();

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

        System.out.println("\n════════════════════════════════════");
        System.out.println("⚔️  " + "¡Todo listo para la batalla!" + " ⚔️");
        System.out.println("   Presione Enter para continuar...");
        System.out.println("════════════════════════════════════");
        scanner.nextLine();
    }

    private void posicionarLord(Bando bando, Tablero tablero) {
        Unidad lord = juego.getLordDeReserva(bando);
                
        if (lord == null) {
            throw new IllegalStateException("No se encontró un Lord para el " + bando);
        }
        
        boolean posicionadoConExito = false;
        while (!posicionadoConExito) {
            String tableroStr = TableroRenderer.render(tablero, bando);
            vInicio.mostrarTablero(tableroStr);
            UbicacionInicio ubi = vInicio.pedirUbicacionLord(bando, tablero.getFilas(), tablero.getColumnas());
            
            Casilla c = tablero.getCasilla(ubi.getFila(), ubi.getColumna());
            
            if (c != null && c.esTransitable() && !c.estaOcupada()) {
                juego.desplegarUnidad(lord, ubi.getFila(), ubi.getColumna());
                System.out.println("✔ Lord " + lord.getNombre() + " desplegado en (" + ubi.getFila() + "," + ubi.getColumna() + ").");
                posicionadoConExito = true;
                break; 
            }
            System.out.println(Colores.WARNING + "¡Ubicación inválida! La casilla no es transitable o está ocupada." + Colores.RESET);
        }
    }

    private void faseDeDespliegue(Bando bando, Tablero tablero) {
            System.out.println("\n══════════ FASE DE DESPLIEGUE: " + bando + " ══════════");
            System.out.println("\n⚠️  " + "Aviso: Las unidades de tu bando solo pueden desplegarse en casillas adyacentes a su Lord");
            
            boolean despliegueCompleto = false;
            while (!despliegueCompleto) {
                List<Unidad> enReserva = juego.getUnidadesEnReserva(bando);
                if (enReserva.isEmpty()) {
                    System.out.println("No quedan unidades en la reserva.");
                    despliegueCompleto = true;
                    break;
                }
    
                String tableroStr = TableroRenderer.render(tablero, bando);
                vInicio.mostrarTablero(tableroStr);
                Unidad unidadADesplegar = null;

                if (enReserva.isEmpty()) {
                    System.out.println("No hay más unidades en la reserva.");
                    
                } else {
                    String bandoNombre = bando.toString();
                    String bandoColor = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;

                    List<String> nombresUnidades = new ArrayList<>();
                    List<String> equipsUnidades = new ArrayList<>();
                    
                    for (Unidad u : enReserva) {
                        nombresUnidades.add(u.getNombre());
                        String eq;
                        if (u.getEquipamiento() != null) {
                            eq = u.getEquipamiento().getNombre();
                        } else {
                            eq = "Puño limpio";
                        }
                        equipsUnidades.add(eq);
                    }
                    int idx = vInicio.seleccionarUnidadDeReserva(bandoNombre, bandoColor, nombresUnidades, equipsUnidades);
                    if (idx == 0) {
                        unidadADesplegar = null;
                    } else {
                        unidadADesplegar = enReserva.get(idx - 1);
                    }
                }
    
                if (unidadADesplegar == null) {
                    System.out.println("Terminando fase de despliegue para " + bando);
                    break;
                }
    
                String nombreUnidad = unidadADesplegar.getNombre();
                String bandoNombre = bando.toString();
                String bandoColor = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
                UbicacionInicio ubi = vInicio.pedirUbicacionUnidad(nombreUnidad, bandoNombre, bandoColor, tablero.getFilas(), tablero.getColumnas());
                boolean desplegada = juego.desplegarUnidad(unidadADesplegar, ubi.getFila(), ubi.getColumna());
    
                if (desplegada) {
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
                    System.out.println(Colores.WARNING + "Despliegue fallido. Intente en otra casilla." + Colores.RESET);
                }
            }
        }
}