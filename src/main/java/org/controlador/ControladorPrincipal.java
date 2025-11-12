package org.controlador;

import org.controlador.tipos.ControladorInicio;
import org.controlador.tipos.ControladorTurno;
import org.controlador.tipos.ControladorUnidades;
import org.modelo.Juego;
import org.vista.VistaPrincipal;

public class ControladorPrincipal implements Controlador {
    private ControladorInicio cInicio;
    private ControladorTurno cTurno;
    private ControladorUnidades cUnidades;

    public ControladorPrincipal(Juego juego, VistaPrincipal vista) {
        this.cInicio = new ControladorInicio(juego, vista.getvInicio());
        this.cUnidades = new ControladorUnidades(juego, vista.getvUnidades());
        this.cTurno = new ControladorTurno(juego, vista.getvTurno(), cUnidades);
    }

    @Override
    public void ejecutar() {
        try {
            cInicio.ejecutar();
            cTurno.ejecutar();
        } catch (Exception e) {
            System.err.println("Ha ocurrido un error fatal que detuvo el juego.");
            e.printStackTrace();
        }
    }
}