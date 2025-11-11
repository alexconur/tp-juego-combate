package org.controlador.acciones;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelo.Juego;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.tipos.VistaTurno;

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
    public boolean ejecutar() {
        List<Unidad> actuables = juego.getUnidadesEnTablero(bando).stream()
                .filter(Unidad::puedeActuar)
                .filter(Unidad::estaVivo)
                .collect(Collectors.toList());

        if (actuables.isEmpty()) {
            System.out.println("No hay unidades disponibles para actuar.");
            return true;
        }

        Unidad u = seleccionarUnidadDeLista(actuables, "Actuar", bando);
        if (u == null) {
            return true;
        }

        boolean esCuracion = (u.getEquipamiento() != null && !u.getEquipamiento().esOfensivo());
        Bando bandoEnemigo = (bando == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;
        Bando bandoObjetivo = esCuracion ? bando : bandoEnemigo;
        String promptObjetivo = esCuracion ? "Curar" : "Atacar";

        List<Unidad> objetivos = juego.getUnidadesEnTablero(bandoObjetivo).stream()
                .filter(Unidad::estaVivo)
                .collect(Collectors.toList());
        
        if (objetivos.isEmpty()) {
            System.out.println("No hay objetivos válidos para " + promptObjetivo + ".");
            return true;
        }

        Unidad objetivo = seleccionarUnidadDeLista(objetivos, promptObjetivo, bandoObjetivo);
        if (objetivo == null) {
            return true;
        }

        ejecutarAtaqueOCuracion(u, objetivo, esCuracion);
        
        return true;
    }

    private Unidad seleccionarUnidadDeLista(List<Unidad> unidades, String prompt, Bando bandoDelColor) {
        if (unidades.isEmpty()) {
            return null;
        }
        
        List<String> lineasFormateadas = new ArrayList<>();
        int i = 1;

        for (Unidad unidad : unidades) {
            String pos = (unidad.getCasillaActual() != null) ?
                    "(" + unidad.getPosFila() + "," + unidad.getPosColumna() + ")" : "(RESERVA)";
            String hp = unidad.getHp() + "/" + unidad.getMaxHp() + " HP";
            String linea = String.format("[%d] %-15s %-10s %s",
                    i++, unidad.getNombre(), pos, hp);
            lineasFormateadas.add(linea);
        }

        String color = (bandoDelColor == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
        int idx = vTurno.seleccionarUnidad(prompt, lineasFormateadas, color);
        if (idx > 0) {
            return unidades.get(idx - 1);
        }
        
        return null;
    }

    private void ejecutarAtaqueOCuracion(Unidad actor, Unidad objetivo, boolean esCuracion) {
        int rango = actor.getEquipamiento().getRango();
        int dist = actor.distanciaA(objetivo);

        if (dist > rango) {
            System.out.println("Objetivo fuera de rango (" + rango + ").");
            return;
        }

        if (esCuracion) {
            actor.curarAliado(objetivo);
            System.out.println("🩹 " + actor.getNombre() + " curó a " + objetivo.getNombre() + ".");
        } else {
            actor.atacar(objetivo);
            System.out.println("💥 " + actor.getNombre() + " atacó a " + objetivo.getNombre() + ".");
        }

        if (!objetivo.estaVivo() && objetivo.getCasillaActual() != null) {
            objetivo.desocuparCasilla();
            System.out.println("💀 ¡" + objetivo.getNombre() + " ha sido derrotado!");
        }
    }
}
