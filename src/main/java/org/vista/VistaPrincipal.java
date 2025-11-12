package org.vista;

import org.vista.tipos.*;

public class VistaPrincipal {
    private final VistaInicio vInicio;
    private final VistaTurno vTurno;
    private final VistaUnidades vUnidades;

    public VistaPrincipal() {
        this.vInicio = new VistaInicio();
        this.vTurno = new VistaTurno();
        this.vUnidades = new VistaUnidades();
    }

    public VistaInicio getvInicio() { return vInicio; }
    public VistaTurno getvTurno() { return vTurno; }
    public VistaUnidades getvUnidades() { return vUnidades; }
}
