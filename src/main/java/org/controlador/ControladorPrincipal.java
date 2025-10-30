package org.controlador;

import org.controlador.tipos.ControladorInicio;
import org.controlador.tipos.ControladorTurno;
import org.controlador.tipos.ControladorUnidades;
import org.modelo.Juego;
import org.vista.VistaPrincipal;

public class ControladorPrincipal implements Controlador {
    private Juego juego;
    private VistaPrincipal vista;
    private ControladorInicio cInicio;
    private ControladorTurno cTurno;
    private ControladorUnidades cUnidades;
    // *I* los distintos controladores van como atributos, en caso
    // de crecer mucho los guardamos en una lista o similar (Vista igual)

    public ControladorPrincipal(Juego juego, VistaPrincipal vista) {
        this.juego = juego;
        this.vista = vista;
        this.cInicio = new ControladorInicio(juego, vista.getvInicio());
        this.cTurno = new ControladorTurno(juego, vista.getvTurno());
        this.cUnidades = new ControladorUnidades(juego, vista.getvUnidades());
    }

    @Override
    public void ejecutar() {
        // Lógica para iniciar el juego
        cInicio.ejecutar();


    }
}
