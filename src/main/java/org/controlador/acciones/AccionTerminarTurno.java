package org.controlador.acciones;

public class AccionTerminarTurno implements Accion {
    @Override
    public boolean ejecutar(){ return false; }

    @Override
    public boolean necesitaPausa() { return false; }
}
