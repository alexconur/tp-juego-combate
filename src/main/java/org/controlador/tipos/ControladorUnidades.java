package org.controlador.tipos;

import org.controlador.Controlador;
import org.vista.tipos.VistaUnidades;
import org.modelo.Juego;

public class ControladorUnidades implements Controlador {
    private final Juego juego;
    private final VistaUnidades vUnidades;

    public ControladorUnidades(Juego juego, VistaUnidades vUnidades) {
        this.juego = juego;
        this.vUnidades = vUnidades;
    }

    @Override
    public void ejecutar() {
        
    }
}
