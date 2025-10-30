package org.controlador.tipos;

import org.controlador.Controlador;
import org.vista.tipos.VistaTurno;
import org.modelo.Juego;

public class ControladorTurno implements Controlador {
    private final Juego juego;
    private final VistaTurno vTurno;

    public ControladorTurno(Juego juego, VistaTurno vTurno) {
        this.juego = juego;
        this.vTurno = vTurno;
    }

    @Override
    public void ejecutar() {
        // Lógica para ejecutar el controlador de turnos
    }
}
