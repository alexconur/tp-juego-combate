package org.controlador.acciones.unidades;

import org.modelo.Juego;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaUnidades;
import java.util.List;

public class AccionListarEnTablero implements AccionUnidades {
    private final Juego juego;
    private final VistaUnidades vUnidades;

    public AccionListarEnTablero(Juego juego, VistaUnidades vUnidades) {
        this.juego = juego;
        this.vUnidades = vUnidades;
    }

    @Override
    public boolean ejecutar() {
        vUnidades.limpiarPantalla();
        List<Unidad> unidades = juego.getTodasUnidadesEnTablero();
        
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades desplegadas actualmente.");
        } else {
            vUnidades.mostrarListaUnidades("Unidades desplegadas", unidades);
        }
        return true;
    }
}