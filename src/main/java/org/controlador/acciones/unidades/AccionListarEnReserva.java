package org.controlador.acciones.unidades;

import java.util.ArrayList;
import java.util.List;

import org.modelo.Juego;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaUnidades;
import org.vista.Colores;

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
            List<String> nombresReserva = new ArrayList<>();
            List<String> equipsReserva = new ArrayList<>();
            List<String> coloresReserva = new ArrayList<>();
            for (Unidad u : reserva) {
                nombresReserva.add(u.getNombre());
                String eq = (u.getEquipamiento() != null) ? u.getEquipamiento().getNombre() : "Puño limpio";
                equipsReserva.add(eq);
                coloresReserva.add(Colores.colorParaBando(u.getBando()));
            }
            vUnidades.mostrarListaUnidades("Unidades en reserva", nombresReserva, equipsReserva, coloresReserva);
        }        
        return true;
    }
}