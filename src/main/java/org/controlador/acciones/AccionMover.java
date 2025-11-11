package org.controlador.acciones;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.TableroRenderer;
import org.vista.tipos.UbicacionInicio;
import org.vista.tipos.VistaTurno;

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
        List<Unidad> movibles = juego.getUnidadesEnTablero(bando).stream()
            .filter(Unidad::puedeMoverse)
            .filter(Unidad::estaVivo)
            .collect(Collectors.toList());

        if (movibles.isEmpty()) {
            System.err.println("No hay unidades disponibles para mover.");
            return true;
        }
        
        String prompt = "Mover";

        String colorLista = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;

        List<String> lineasFormateadas = new ArrayList<>();
        int i = 1;
        for (Unidad u_mov : movibles) {
            String pos = (u_mov.getCasillaActual() != null) ? 
                         "(" + u_mov.getCasillaActual().getFila() + "," + u_mov.getCasillaActual().getColumna() + ")" : "(RESERVA)";
            String hp = u_mov.getHp() + "/" + u_mov.getMaxHp() + " HP";
            
            String linea = String.format("[%d] %-15s %-10s %s", i++, u_mov.getNombre(), pos, hp);
            lineasFormateadas.add(linea);
        }

        int idx = vTurno.seleccionarUnidad(prompt, lineasFormateadas, colorLista);

        Unidad u = null;
        if (idx == 0) {
            return true;
        } else {
            u = movibles.get(idx - 1);
        }

        List<Casilla> alcanzables = juego.getTablero().obtenerCasillasAlcanzables(u);
        
        if (alcanzables == null || alcanzables.isEmpty()) {
            System.err.println("No hay casillas alcanzables para esta unidad.");
        } else {
            Tablero tablero = juego.getTablero();
            String tableroStr = TableroRenderer.render(tablero, bando, alcanzables);
            vTurno.mostrarCasillasDisponibles(tableroStr);
        }

        UbicacionInicio ubi = vTurno.pedirUbicacion("Seleccione destino para moverse. Tus movimientos posibles están marcados en el tablero.");
        Casilla destino = juego.getTablero().getCasilla(ubi.getFila(), ubi.getColumna());

        if (!alcanzables.contains(destino)) {
            System.err.println("Movimiento inválido: la casilla no está dentro del rango.");
            return true;
        }
        u.moverA(juego.getTablero(), ubi.getFila(), ubi.getColumna());
        System.err.println("✅ " + u.getNombre() + " se movió a (" + ubi.getFila() + ", " + ubi.getColumna() + ").");
        return true;
    }
}
