// *NUEVO* - Archivo completo
package org.controlador.tipos;

import org.controlador.Controlador;
import org.vista.tipos.VistaInicio;
import org.vista.tipos.VistaTurno;
import org.vista.tipos.VistaUnidades;
import org.modelo.Juego;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorTurno implements Controlador {
    private final Juego juego;
    private final VistaTurno vTurno;

    public ControladorTurno(Juego juego, VistaTurno vTurno) {
        this.juego = juego;
        this.vTurno = vTurno;
    }

    @Override
    public void ejecutar() {
        System.out.println("\n=== ¡COMIENZA EL COMBATE! ===");

        // Bucle principal del juego
        while (!juego.isGameOver()) {
            Bando bandoActual = juego.getBandoActual();
            vTurno.mostrarEstado(juego);
            
            // Bucle de acciones para el jugador actual
            boolean turnoActivo = true;
            while (turnoActivo) {
                int opcion = vTurno.mostrarMenuPrincipal();
                
                switch (opcion) {  //*M* esto podria romper OPC, podria hacerse en un enum (??)
                    case 1: // Mover
                        accionMover(bandoActual);
                        break;
                    case 2: // Atacar / Curar
                        accionActuar(bandoActual);
                        break;
                    case 3: // Ver unidades / detalles
                        ControladorUnidades cu = new ControladorUnidades(juego, new VistaUnidades());
                        cu.ejecutar();
                        break;
                    case 4: // Desplegar
                        accionDesplegar(bandoActual);
                        break;
                    case 5: // Terminar Turno
                        turnoActivo = false;
                        break;
                    default:
                        System.out.println("Opción inválida. Intente de nuevo.");

                }
                
                // Si la opción no fue 'Terminar Turno', mostrar el estado actualizado
                if (turnoActivo && !juego.isGameOver()) {
                    vTurno.mostrarEstado(juego);
                }
            }
            
            // Cambiar de turno
            if (!juego.isGameOver()) {
                juego.cambiarTurno();
            }
        }
        
        System.out.println("=== FIN DE LA PARTIDA ===");
        vTurno.mostrarEstado(juego); // Mostrar estado final
    }
    
    private void accionMover(Bando bando) {
        // 1. Filtrar unidades que pueden moverse
        List<Unidad> movibles = juego.getUnidadesEnTablero(bando).stream()
            .filter(Unidad::puedeMoverse)
            .filter(Unidad::estaVivo)
            .collect(Collectors.toList());
        if (movibles.isEmpty()) return;
        
        // 2. Vista selecciona unidad
        Unidad u = vTurno.seleccionarUnidad(movibles, "Mover");
        if (u == null) return; // Canceló

        // 3. Vista pide coordenadas
        VistaInicio.Ubicacion ubi = vTurno.pedirUbicacion("Mover a");
        int fila = ubi.getFila();
        int col = ubi.getColumna(); 

        // Validar rango de movimiento
        int distancia = Math.max(
                Math.abs(u.getCasillaActual().getFila() - fila),
                Math.abs(u.getCasillaActual().getColumna() - col)
        );

        if (distancia > u.getMovimientoRestante()) {
            System.out.println("Movimiento inválido: destino fuera del rango de movimiento.");
            return;
        }
        
        // 4. Modelo ejecuta
        u.moverA(juego.getTablero(), ubi.getFila(), ubi.getColumna());

        // Marcar movimiento realizado
        u.setMovimientoRestante(0);
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
            System.out.println("💚 " + u.getNombre() + " curó a " + objetivo.getNombre() + ".");
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

        // 2. Vista selecciona unidad
        Unidad u = vTurno.seleccionarUnidadReserva(reserva);
        if (u == null) return; // Canceló

        // 3. Pedir ubicación
        // *NOTA*: El PDF dice que se despliega desde el Lord[cite: 56].
        // Esta lógica de validación de adyacencia al Lord debería
        // estar en el modelo (Juego.desplegarUnidad).
        // Por simplicidad, aquí solo pedimos coords.
        VistaInicio.Ubicacion ubi = vTurno.pedirUbicacion("Desplegar en");

        // 4. Modelo ejecuta
        juego.desplegarUnidad(u, ubi.getFila(), ubi.getColumna());
        System.out.println("✅ Unidad desplegada en (" + ubi.getFila() + "," + ubi.getColumna() + ").");
    }
}