package org.controlador.acciones;

import java.util.ArrayList;
import java.util.List;

import org.modelo.Juego;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.tipos.UbicacionInicio;
import org.vista.tipos.VistaTurno;

public class AccionDesplegar implements Accion {
    private final Juego juego;
    private final VistaTurno vTurno;
    private final Bando bando;

    public AccionDesplegar(Juego juego, VistaTurno vTurno, Bando bando) {
        this.juego = juego;
        this.vTurno = vTurno;
        this.bando = bando;
    }

    @Override
    public boolean ejecutar(){
        List<Unidad> reserva = juego.getUnidadesEnReserva(bando);

        if (reserva.isEmpty()) {
            System.out.println("No hay unidades en la reserva.");
            return true;
        }
        List<String> lineasFormateadas = new ArrayList<>();
        int i = 1; 
        for (Unidad u_res : reserva) {
            String eq = (u_res.getEquipamiento() != null) ? u_res.getEquipamiento().getNombre() : "Puño limpio";
            String linea = String.format("[%d] %-15s (%s)", i++, u_res.getNombre(), eq); 
            lineasFormateadas.add(linea);
        }
        String colorLista = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
        int idx = vTurno.seleccionarUnidadReserva(lineasFormateadas, colorLista);

        Unidad u = null;
        if (idx == 0) {
            return true;
        } else {
            u = reserva.get(idx - 1);
        }

        UbicacionInicio ubi = null;
        boolean desplegada = false;
        
        System.out.println("");
        ubi = vTurno.pedirUbicacion("Desplegar en (fila, columna) adyacente a Lord");

        desplegada = juego.desplegarUnidad(u, ubi.getFila(), ubi.getColumna());
        if (desplegada){
            System.out.println("✅ Unidad desplegada en (" 
                + ubi.getFila() + "," + ubi.getColumna() + ").");
        } else {
            System.out.println("Despliegue fallido. Puede seleccionar otra unidad o cancelar.");
        }

        return true;        
    }
}
