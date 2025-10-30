package org.controlador.tipos;

import org.controlador.Controlador;
import org.modelo.Juego;
import org.vista.tipos.VistaInicio;

public class ControladorInicio implements Controlador {
    private final Juego juego;
    private final VistaInicio vInicio;

    public ControladorInicio(Juego juego, VistaInicio vInicio) {
        this.juego = juego;
        this.vInicio = vInicio;
    }

    @Override
    public void ejecutar() {
        vInicio.mostrar();

        // *I* Acá se debería ir retornando la info seleccionada por el usuario
        // tablero = vInicio.seleccionarMapa();
        vInicio.pedirMapa();
        juego.cargarTablero();

        vInicio.pedirEjercito();

        vInicio.pedirUbicarLord();
    }
}
