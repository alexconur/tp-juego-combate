package org.controlador.tipos;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.controlador.Controlador;
import org.controlador.acciones.*;
import org.modelo.Juego;
import org.modelo.unidades.Bando;
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

    //Inicializa el mapa de acciones para el bando que tiene el turno.
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

        // Bucle principal del juego
        while (!juego.isGameOver()) {
            Bando bandoActual = juego.getBandoActual();
            inicializarAcciones(bandoActual);

            // Bucle de acciones para el jugador actual
            boolean turnoActivo = true;
            while (turnoActivo) {
                vTurno.limpiarPantalla();
                vTurno.mostrarEstado(juego);
            
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
            
            // Cambiar de turno
            if (!juego.isGameOver()) {
                juego.cambiarTurno();
            } else {
                System.out.println("\n🏁 ¡El juego ha finalizado!");
                break;
            }
        }
        
        System.out.println("=== FIN DE LA PARTIDA ===");
        vTurno.mostrarEstado(juego); // Mostrar estado final
    }    

    private void pausarParaContinuar(){
        System.out.println("Presione Enter para continuar...");
        sc.nextLine();
    }
}