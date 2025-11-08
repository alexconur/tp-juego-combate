package org.controlador.acciones.unidades;

import org.modelo.Juego;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaUnidades;
import java.util.List;

public class AccionVerDetalle implements AccionUnidades {
    private final Juego juego;
    private final VistaUnidades vUnidades;

    public AccionVerDetalle(Juego juego, VistaUnidades vUnidades) {
        this.juego = juego;
        this.vUnidades = vUnidades;
    }

    @Override
    public boolean ejecutar() {
        vUnidades.limpiarPantalla();
        List<Unidad> unidades = juego.getTodasUnidadesEnTablero();
        Unidad seleccionada = vUnidades.seleccionarUnidadViva(unidades);
        
        if (seleccionada != null) {
            vUnidades.mostrarDetalleUnidad(seleccionada);
        }
        return true;
    }
}