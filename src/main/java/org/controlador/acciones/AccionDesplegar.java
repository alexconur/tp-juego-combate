package org.controlador.acciones;

import org.modelo.Juego;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaInicio;
import org.vista.tipos.VistaTurno;

import java.util.List;

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

        VistaInicio.Ubicacion ubi = null;
        boolean desplegada = false;
        
        Unidad u = vTurno.seleccionarUnidadReserva(reserva, juego);
        if (u == null) return true;

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
