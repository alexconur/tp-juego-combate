package org.controlador.acciones;

import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaTurno;

import java.util.List;
import java.util.stream.Collectors;

public class AccionEmboscada implements Accion {
    private final Juego juego;
    private final VistaTurno vTurno;
    private final Bando bando;

    public AccionEmboscada(Juego juego, VistaTurno vTurno, Bando bando) {
        this.juego = juego;
        this.vTurno = vTurno;
        this.bando = bando;
    }

    @Override
    public boolean ejecutar(){
        List<Unidad> disponibles = juego.getUnidadesEnTablero(bando).stream()
                .filter(Unidad::estaVivo)
                .filter(Unidad::puedePrepararEmboscada)
                .collect(Collectors.toList());

        if (disponibles.isEmpty()) {
            System.out.println("No hay unidades que puedan preparar emboscada.");
            return true;
        }

        Unidad u = vTurno.seleccionarUnidad(disponibles, "Preparar emboscada", juego);
        if (u == null) return true;

        Casilla c = u.getCasillaActual();
        if (c == null || !c.permiteEmboscada()) {
            vTurno.mostrarEmboscadaInvalida(u);
            return true;
        }

        u.prepararEmboscada();
        vTurno.mostrarEmboscadaExitosa(u);
        return true;
    }
}
