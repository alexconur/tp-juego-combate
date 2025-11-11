package org.controlador.acciones;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.tipos.VistaTurno;

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

        String prompt = "Preparar emboscada";

        String colorLista = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;

        List<String> lineasFormateadas = new ArrayList<>();
        int i = 1;
        for (Unidad u : disponibles) {
            String pos = (u.getCasillaActual() != null) ? 
                         "(" + u.getPosFila() + "," + u.getPosColumna() + ")" : "(RESERVA)";
            String hp = u.getHp() + "/" + u.getMaxHp() + " HP";

            String linea = String.format("[%d] %-15s %-10s %s", i++, u.getNombre(), pos, hp);
            lineasFormateadas.add(linea);
        }
        int idx = vTurno.seleccionarUnidad(prompt, lineasFormateadas, colorLista);

        Unidad u = null;
        if (idx == 0) {
            return true;
        } else {
            u = disponibles.get(idx - 1);
        }
        
        Casilla c = u.getCasillaActual();
        if (c == null || !c.permiteEmboscada()) {
            vTurno.mostrarEmboscadaInvalida(u.getNombre());
            return true;
        }

        u.prepararEmboscada();
        vTurno.mostrarEmboscadaExitosa(u.getNombre());
        return true;
    }
}
