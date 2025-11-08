package org.controlador.acciones;

import org.vista.tipos.VistaTurno;
import org.controlador.tipos.ControladorUnidades;

public class AccionVerUnidades implements Accion {
    private final VistaTurno vTurno;
    private final ControladorUnidades cUnidades;

    public AccionVerUnidades(VistaTurno vTurno, ControladorUnidades cUnidades) {
        this.vTurno = vTurno;
        this.cUnidades = cUnidades;
    }

    @Override
    public boolean ejecutar() {
        vTurno.limpiarPantalla();
        cUnidades.ejecutar();
        return true;
    }

    @Override
    public boolean necesitaPausa() { return false; }
}