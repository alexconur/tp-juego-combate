// *NUEVO* - Archivo completo
package org.controlador.tipos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.controlador.Controlador;
import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.casillas.Bosque;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaInicio;
import org.vista.tipos.VistaTurno;

public class ControladorTurno implements Controlador {
    private final Juego juego;
    private final VistaTurno vTurno;
    private final ControladorUnidades cUnidades;

    public ControladorTurno(Juego juego, VistaTurno vTurno, ControladorUnidades cUnidades) {
        this.juego = juego;
        this.vTurno = vTurno;
        this.cUnidades = cUnidades;
    }

    @Override
    public void ejecutar() {
        System.out.println("\n=== ¡COMIENZA EL COMBATE! ===");

        // Bucle principal del juego
        while (!juego.isGameOver()) {
            Bando bandoActual = juego.getBandoActual();

            // Bucle de acciones para el jugador actual
            boolean turnoActivo = true;
            while (turnoActivo) {
                vTurno.limpiarPantalla();
                vTurno.mostrarEstado(juego);
            
                int opcion = vTurno.mostrarMenuPrincipal();
                
                switch (opcion) {  //*M* esto podria romper OCP, podria hacerse en un enum .. *A* lo intenté y me quedó feo
                    case 1: // Mover
                        accionMover(bandoActual);
                        break;
                    case 2: // Atacar / Curar
                        accionActuar(bandoActual);
                        break;
                    case 3: // Ver unidades / detalles
                        cUnidades.ejecutar();
                        break;
                    case 4: // Desplegar
                        accionDesplegar(bandoActual);
                        break;
                    case 5: // Preparar Emboscada
                        accionPrepararEmboscada(bandoActual);
                        break;
                    case 6: // Info Casillas
                        vTurno.limpiarPantalla();
                        infoCasillas();
                        break;
                    case 7: // Terminar Turno
                        turnoActivo = false;
                        break;
                    case 8: // Rendirse
                        juego.rendirse(juego.getBandoActual());
                        turnoActivo = false;
                        break;    
                    default:
                        System.out.println("Opción inválida. Intente de nuevo.");
                        break;

                }
                if (juego.isGameOver()) {
                    turnoActivo = false;
                    break;
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
    
    private void infoCasillas(){
        vTurno.mostrarInfoCasillas();
    }

    private void accionMover(Bando bando) {
        // 1. Filtrar unidades que pueden moverse
        List<Unidad> movibles = juego.getUnidadesEnTablero(bando).stream()
            .filter(Unidad::puedeMoverse)
            .filter(Unidad::estaVivo)
            .collect(Collectors.toList());
        if (movibles.isEmpty()) {
            System.out.println("No hay unidades disponibles para mover.");
            return;
        }
        
        // 2. Vista selecciona unidad
        Unidad u = vTurno.seleccionarUnidad(movibles, "Mover");
        if (u == null) return; // Canceló

        // 3. Calcular casillas alcanzables usando BFS
        List<Casilla> alcanzables = juego.getTablero().obtenerCasillasAlcanzables(u);

        // 4. Mostrar al jugador las casillas donde puede moverse
        vTurno.mostrarCasillasDisponibles(alcanzables);

        // 5. Pedir destino
        VistaInicio.Ubicacion ubi = vTurno.pedirUbicacion("Seleccione destino para moverse");
        Casilla destino = juego.getTablero().getCasilla(ubi.getFila(), ubi.getColumna());

        // 6. Validar destino
        if (!alcanzables.contains(destino)) {
            System.out.println("Movimiento inválido: la casilla no está dentro del rango.");
            return;
        }

        // 7. Ejecutar movimiento
        u.moverA(juego.getTablero(), ubi.getFila(), ubi.getColumna());
        u.setMovimientoRestante(0);
        System.out.println("✅ " + u.getNombre() + " se movió a (" + ubi.getFila() + ", " + ubi.getColumna() + ").");
    }

    private void accionActuar(Bando bando) {
        // 1. Filtrar unidades que pueden actuar
        List<Unidad> actuables = juego.getUnidadesEnTablero(bando).stream()
            .filter(Unidad::puedeActuar)
            .filter(Unidad::estaVivo)
            .collect(Collectors.toList());
        if (actuables.isEmpty()) return;

        // 2. Vista selecciona unidad
        Unidad u = vTurno.seleccionarUnidad(actuables, "Atacar/Curar");
        if (u == null) return; // Canceló

        // 3. Determinar si es ofensivo o de curación
        boolean esCuracion = (u.getEquipamiento() != null && !u.getEquipamiento().esOfensivo());

        // 4. Buscar objetivos
        List<Unidad> objetivos = new ArrayList<>();
        
        if (esCuracion) {
            // Si cura, los objetivos son aliados [cite: 125]
            objetivos = juego.getUnidadesEnTablero(bando).stream()
                .filter(Unidad::estaVivo)
                .collect(Collectors.toList());
        } else {
            // Si ataca, los objetivos son enemigos
            Bando bandoEnemigo = (bando == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;
            objetivos = juego.getUnidadesEnTablero(bandoEnemigo).stream()
                .filter(Unidad::estaVivo)
                .collect(Collectors.toList());
        }

        // 5. Vista selecciona objetivo
        Unidad objetivo = vTurno.seleccionarUnidad(objetivos, esCuracion ? "Curar" : "Atacar");
        if (objetivo == null) return;

        // Validar rango
        int rango = u.getEquipamiento().getRango();
        int dist = Math.max(
                Math.abs(u.getCasillaActual().getFila() - objetivo.getCasillaActual().getFila()),
                Math.abs(u.getCasillaActual().getColumna() - objetivo.getCasillaActual().getColumna())
        );

        if (dist > rango) {
            System.out.println("Objetivo fuera de rango (" + rango + ").");
            return;
        }

        // Ejecutar acción
        if (esCuracion) {
            u.curarAliado(objetivo);
            System.out.println("🩹 " + u.getNombre() + " curó a " + objetivo.getNombre() + ".");
        } else {
            u.atacar(objetivo);
            System.out.println("💥 " + u.getNombre() + " atacó a " + objetivo.getNombre() + ".");
        }

        // No necesitamos setear flags manualmente, Unidad.atacar() ya lo hace
        if (!objetivo.estaVivo() && objetivo.getCasillaActual() != null) {
            objetivo.getCasillaActual().desocupar();
            System.out.println("💀 ¡" + objetivo.getNombre() + " ha sido derrotado!");
        }

    }
    
    private void accionDesplegar(Bando bando) {
        // 1. Obtener unidades en reserva
        List<Unidad> reserva = juego.getUnidadesEnReserva(bando);
        
        /*
        // 2. Vista selecciona unidad
        Unidad u = vTurno.seleccionarUnidadReserva(reserva);
        if (u == null) return; // Canceló

        // 3. Pedir ubicación
        // *NOTA*: El PDF dice que se despliega desde el Lord[cite: 56].
        // Esta lógica de validación de adyacencia al Lord debería
        // estar en el modelo (Juego.desplegarUnidad).
        // Por simplicidad, aquí solo pedimos coords.
        VistaInicio.Ubicacion ubi = vTurno.pedirUbicacion("Desplegar en");
        */

        // 4. Modelo ejecuta
        VistaInicio.Ubicacion ubi = null;
        boolean desplegada = false;
        while (!desplegada && !reserva.isEmpty()) {
            Unidad u = vTurno.seleccionarUnidadReserva(reserva);
            if (u == null) return; // Canceló

            ubi = vTurno.pedirUbicacion("Desplegar en (fila, columna)");
            
            // Intentar desplegar
            desplegada = juego.desplegarUnidad(u, ubi.getFila(), ubi.getColumna());
            if (!desplegada) {
                System.out.println("Despliegue fallido. Puede seleccionar otra unidad o cancelar.");
                // Remover esta unidad de la lista temporal para no repetir automáticamente
                // Si querés permitir reintentos con la misma unidad, no hagas esto
                // reserva.remove(u);
            }
        }

        if (desplegada && ubi != null) {
        System.out.println("✅ Unidad desplegada en (" 
            + ubi.getFila() + "," + ubi.getColumna() + ").");
        } else {
            System.out.println("No se desplegó ninguna unidad.");
        }
        
    }

    private void accionPrepararEmboscada(Bando bando) {
        List<Unidad> disponibles = juego.getUnidadesEnTablero(bando).stream()
                .filter(Unidad::estaVivo)
                .filter(Unidad::puedePrepararEmboscada)
                .collect(Collectors.toList());

        if (disponibles.isEmpty()) {
            System.out.println("No hay unidades que puedan preparar emboscada.");
            return;
        }

        Unidad u = vTurno.seleccionarUnidad(disponibles, "Preparar emboscada");
        if (u == null) return;

        Casilla c = u.getCasillaActual();
        if (c == null || !(c instanceof Bosque)) {
            vTurno.mostrarEmboscadaInvalida(u);
            return;
        }

        u.prepararEmboscada();
        vTurno.mostrarEmboscadaExitosa(u);
    }
}