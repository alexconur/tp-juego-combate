package org.controlador.acciones;

import org.modelo.Juego;
import org.modelo.unidades.Bando;

public class AccionRendirse implements Accion {
    private final Juego juego;
    private final Bando bando;

    public AccionRendirse(Juego juego, Bando bando) {
        this.juego = juego;
        this.bando = bando;
    }

    @Override
    public boolean ejecutar() {
        juego.rendirse(bando);
        return false;
    }

    @Override
    public boolean necesitaPausa() { return false; }
}