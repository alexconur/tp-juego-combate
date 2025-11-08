package org.controlador.acciones.unidades;

import org.modelo.Juego;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaUnidades;
import java.util.List;

public class AccionListarEnReserva implements AccionUnidades {
    private final Juego juego;
    private final VistaUnidades vUnidades;

    public AccionListarEnReserva(Juego juego, VistaUnidades vUnidades) {
        this.juego = juego;
        this.vUnidades = vUnidades;
    }

    @Override
    public boolean ejecutar() {
        vUnidades.limpiarPantalla();
        List<Unidad> reserva = juego.getTodasUnidadesEnReserva();
        
        if (reserva.isEmpty()) {
            System.out.println("No hay unidades en reserva.");
        } else {
            vUnidades.mostrarListaUnidades("Unidades en reserva", reserva); 
        }
        return true;
    }
}