package org.controlador.acciones.unidades;

import java.util.ArrayList;
import java.util.List;

import org.modelo.Juego;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaUnidades;

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
            List<String> nombresDesplegadas = new ArrayList<>();
            List<String> equipsDesplegadas = new ArrayList<>();
            for (Unidad u : unidades) {
                nombresDesplegadas.add(u.getNombre());
                String eq = (u.getEquipamiento() != null) ? u.getEquipamiento().getNombre() : "Puño limpio";
                equipsDesplegadas.add(eq);
            }
            vUnidades.mostrarListaUnidades("Unidades desplegadas", nombresDesplegadas, equipsDesplegadas);
        }
        return true;
    }
}