package org.controlador.acciones;

import org.vista.tipos.VistaTurno;

public class AccionInfoCasillas implements Accion {
    private final VistaTurno vTurno;

    public AccionInfoCasillas(VistaTurno vTurno) {
        this.vTurno = vTurno;
    }

    @Override
    public boolean ejecutar() {
        vTurno.limpiarPantalla();
        vTurno.mostrarInfoCasillas();
        return true;
    }

    @Override
    public boolean necesitaPausa() { return false; }
}