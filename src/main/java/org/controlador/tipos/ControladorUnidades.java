package org.controlador.tipos;

import org.controlador.Controlador;
import org.modelo.Juego;
import org.vista.tipos.VistaUnidades;
import org.controlador.acciones.unidades.*;

import java.util.HashMap;
import java.util.Map;

public class ControladorUnidades implements Controlador {

    private final VistaUnidades vUnidades;

    private final Map<Integer, AccionUnidades> acciones;
    private final AccionUnidades accionInvalida;

    public ControladorUnidades(Juego juego, VistaUnidades vUnidades) {
        this.vUnidades = vUnidades;
        this.acciones = new HashMap<>();
        acciones.put(1, new AccionListarEnTablero(juego, vUnidades));
        acciones.put(2, new AccionListarEnReserva(juego, vUnidades));
        acciones.put(3, new AccionVerDetalle(juego, vUnidades));
        acciones.put(0, new AccionVolver());
        
        this.accionInvalida = new AccionInvalida();
    }

    @Override
    public void ejecutar() {
        boolean continuar = true;

        while (continuar) {
            int opcion = vUnidades.mostrarMenuPrincipal();

            AccionUnidades accion = acciones.getOrDefault(opcion, accionInvalida);

            continuar = accion.ejecutar();
        }
    }
}