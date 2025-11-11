package org.controlador.tipos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.controlador.Controlador;
import org.controlador.acciones.Accion;
import org.controlador.acciones.AccionActuar;
import org.controlador.acciones.AccionDesplegar;
import org.controlador.acciones.AccionEmboscada;
import org.controlador.acciones.AccionInfoCasillas;
import org.controlador.acciones.AccionMover;
import org.controlador.acciones.AccionRendirse;
import org.controlador.acciones.AccionTerminarTurno;
import org.controlador.acciones.AccionVerUnidades;
import org.modelo.Juego;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.TableroRenderer;
import org.vista.tipos.VistaTurno;

public class ControladorTurno implements Controlador {
    private final Juego juego;
    private final VistaTurno vTurno;
    private final ControladorUnidades cUnidades;
    private final Scanner sc = new Scanner(System.in);
    private Map<Integer, Accion> acciones;

    public ControladorTurno(Juego juego, VistaTurno vTurno, ControladorUnidades cUnidades) {
        this.juego = juego;
        this.vTurno = vTurno;
        this.cUnidades = cUnidades;
        this.acciones = new HashMap<>();
    }

    private void inicializarAcciones(Bando bandoActual) {
        acciones.clear();
        acciones.put(1, new AccionMover(juego, vTurno, bandoActual));
        acciones.put(2, new AccionActuar(juego, vTurno, bandoActual));
        acciones.put(3, new AccionVerUnidades(vTurno, cUnidades));
        acciones.put(4, new AccionDesplegar(juego, vTurno, bandoActual));
        acciones.put(5, new AccionEmboscada(juego, vTurno, bandoActual));
        acciones.put(6, new AccionInfoCasillas(vTurno));
        acciones.put(7, new AccionTerminarTurno());
        acciones.put(8, new AccionRendirse(juego, bandoActual));
    }

    @Override
    public void ejecutar() {
        System.out.println("\n=== ¡COMIENZA EL COMBATE! ===");

        while (!juego.isGameOver()) {
            Bando bandoActual = juego.getBandoActual();
            inicializarAcciones(bandoActual);

            boolean turnoActivo = true;
            while (turnoActivo) {
                vTurno.limpiarPantalla();
                Tablero tablero = juego.getTablero();
                int numeroTurno = juego.getNumeroTurno();
                List<Unidad> unidadesEnTablero = juego.getTodasUnidadesEnTablero();
                boolean isGameOver = juego.isGameOver();
                refrescarVistaEstadoJuego(tablero, bandoActual, numeroTurno, unidadesEnTablero, isGameOver);
            
                int opcion = vTurno.mostrarMenuPrincipal();
                boolean necesitaPausa = false;

                Accion accion = acciones.get(opcion);
                if (accion != null) {
                    turnoActivo = accion.ejecutar();
                    necesitaPausa = accion.necesitaPausa();
                } else {
                    System.out.println("Opción inválida. Intente de nuevo.");
                    necesitaPausa = true;
                }

                if (juego.isGameOver()) {
                    turnoActivo = false;
                } else if(necesitaPausa){
                    pausarParaContinuar();
                }
            }
            
            if (!juego.isGameOver()) {
                juego.cambiarTurno();
            } else {
                System.out.println("\n🏁 ¡El juego ha finalizado!");
                break;
            }
        }
        
        System.out.println("=== FIN DE LA PARTIDA ===");
        Tablero tablero = juego.getTablero();
        Bando bandoActual = juego.getBandoActual();
        int numeroTurno = juego.getNumeroTurno();
        List<Unidad> unidadesEnTablero = juego.getTodasUnidadesEnTablero();
        refrescarVistaEstadoJuego(tablero, bandoActual, numeroTurno, unidadesEnTablero, true);
    }

    private void refrescarVistaEstadoJuego(Tablero tablero, Bando bandoActual, int numeroTurno, List<Unidad> unidadesEnTablero, boolean isGameOver) {
        String bandoNombre = bandoActual.toString();
        String bandoColor = (bandoActual == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;

        String tableroStr = TableroRenderer.render(tablero, bandoActual);

        List<String> lineasDeUnidad = new ArrayList<>();
        List<String> coloresUnidad = new ArrayList<>();

        for (Unidad u : unidadesEnTablero) {
            String bando = u.getBando().toString().substring(6, 12);
            String pos = (u.getCasillaActual() != null) ? 
                         "(" + u.getPosFila() + "," + u.getPosColumna() + ")" : "(RESERVA)";
            String hp = u.getHp() + "/" + u.getMaxHp() + " HP";
            String estado = (u.estaVivo() ? hp : "MUERTO");
            String acciones = "[Mov: " + (u.puedeMoverse() ? "SI" : "NO") + ", Act: " + (u.puedeActuar() ? "SI" : "NO") + "]";  
            String colorBando;
            if (u.estaVivo()){
                colorBando = (u.getBando() ==  Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
            } else {
                colorBando = Colores.VACIO_U;
            }
            String linea = String.format("[%s] %-18s %-10s %-12s %s", bando, u.getNombre(), pos, estado, acciones);
            lineasDeUnidad.add(linea);
            coloresUnidad.add(colorBando);
        }

        this.vTurno.mostrarEstado(isGameOver, numeroTurno, bandoNombre, bandoColor, tableroStr, lineasDeUnidad, coloresUnidad);
    }

    private void pausarParaContinuar(){
        System.out.println("Presione Enter para continuar...");
        sc.nextLine();
    }
}