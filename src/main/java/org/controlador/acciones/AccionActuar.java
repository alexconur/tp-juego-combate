package org.controlador.acciones;

import org.modelo.Juego;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaTurno;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccionActuar implements Accion {
    private final Juego juego;
    private final VistaTurno vTurno;
    private final Bando bando;

    public AccionActuar(Juego juego, VistaTurno vTurno, Bando bando) {
        this.juego = juego;
        this.vTurno = vTurno;
        this.bando = bando;
    }

    @Override
    public boolean ejecutar(){
        // Filtrar unidades que pueden actuar
        List<Unidad> actuables = juego.getUnidadesEnTablero(bando).stream()
            .filter(Unidad::puedeActuar)
            .filter(Unidad::estaVivo)
            .collect(Collectors.toList());

        if (actuables.isEmpty()) {
            System.out.println("No hay unidades disponibles para actuar.");
            return true;
        }

        Unidad u = vTurno.seleccionarUnidad(actuables, "Atacar/Curar", juego);
        if (u == null) return true;

        // Determinar si es ofensivo o de curación
        boolean esCuracion = (u.getEquipamiento() != null && !u.getEquipamiento().esOfensivo());

        // Logica: si cura los objetivos son aliados, si ataca entonces son enemigos
        List<Unidad> objetivos = new ArrayList<>();
        if (esCuracion) {
            objetivos = juego.getUnidadesEnTablero(bando).stream()
                .filter(Unidad::estaVivo)
                .collect(Collectors.toList());
        } else {
            Bando bandoEnemigo = (bando == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;
            objetivos = juego.getUnidadesEnTablero(bandoEnemigo).stream()
                .filter(Unidad::estaVivo)
                .collect(Collectors.toList());
        }

        Unidad objetivo = vTurno.seleccionarUnidad(objetivos, esCuracion ? "Curar" : "Atacar", juego);
        if (objetivo == null) return true;

        // Validar rango
        int rango = u.getEquipamiento().getRango();
        int dist = Math.max(
                Math.abs(u.getCasillaActual().getFila() - objetivo.getCasillaActual().getFila()),
                Math.abs(u.getCasillaActual().getColumna() - objetivo.getCasillaActual().getColumna())
        );

        if (dist > rango) {
            System.out.println("Objetivo fuera de rango (" + rango + ").");
            return true;
        }

        // Ejecutar acción
        if (esCuracion) {
            u.curarAliado(objetivo);
            System.out.println("🩹 " + u.getNombre() + " curó a " + objetivo.getNombre() + ".");
        } else {
            u.atacar(objetivo);
            System.out.println("💥 " + u.getNombre() + " atacó a " + objetivo.getNombre() + ".");
        }

        if (!objetivo.estaVivo() && objetivo.getCasillaActual() != null) {
            objetivo.getCasillaActual().desocupar();
            System.out.println("💀 ¡" + objetivo.getNombre() + " ha sido derrotado!");
        }
        return true;
    }
}
