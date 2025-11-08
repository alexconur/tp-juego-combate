package org.controlador.acciones;

import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaInicio;
import org.vista.tipos.VistaTurno;

import java.util.List;
import java.util.stream.Collectors;

public class AccionMover implements Accion {
    private final Juego juego;
    private final VistaTurno vTurno;
    private final Bando bando;

    public AccionMover(Juego juego, VistaTurno vTurno, Bando bando) {
        this.juego = juego;
        this.vTurno = vTurno;
        this.bando = bando;
    }

    @Override
    public boolean ejecutar(){
        // Filtrar unidades que pueden moverse
        List<Unidad> movibles = juego.getUnidadesEnTablero(bando).stream()
            .filter(Unidad::puedeMoverse)
            .filter(Unidad::estaVivo)
            .collect(Collectors.toList());
        if (movibles.isEmpty()) {
            System.out.println("No hay unidades disponibles para mover.");
            return true;
        }
        
        Unidad u = vTurno.seleccionarUnidad(movibles, "Mover", juego);
        if (u == null) return true;

        // Calcular casillas alcanzables usando BFS
        List<Casilla> alcanzables = juego.getTablero().obtenerCasillasAlcanzables(u);

        // Mostrar al jugador las casillas donde puede moverse
        vTurno.mostrarCasillasDisponibles(alcanzables, juego.getTablero(), bando);

        // Pedir destino
        VistaInicio.Ubicacion ubi = vTurno.pedirUbicacion("Seleccione destino para moverse. Tus movimientos posibles están marcados en el tablero.");
        Casilla destino = juego.getTablero().getCasilla(ubi.getFila(), ubi.getColumna());

        // Validar destino
        if (!alcanzables.contains(destino)) {
            System.out.println("Movimiento inválido: la casilla no está dentro del rango.");
            return true;
        }

        // Ejecutar movimiento
        u.moverA(juego.getTablero(), ubi.getFila(), ubi.getColumna());
        System.out.println("✅ " + u.getNombre() + " se movió a (" + ubi.getFila() + ", " + ubi.getColumna() + ").");
        return true;
    }
}
