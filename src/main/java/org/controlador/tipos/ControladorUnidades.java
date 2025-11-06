package org.controlador.tipos;

import org.controlador.Controlador;
import org.modelo.Juego;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaUnidades;

import java.util.List;

public class ControladorUnidades implements Controlador {

    private final Juego juego;
    private final VistaUnidades vUnidades;

    public ControladorUnidades(Juego juego, VistaUnidades vUnidades) {
        this.juego = juego;
        this.vUnidades = vUnidades;
    }

    @Override
    public void ejecutar() {
        Bando bandoActual = juego.getBandoActual();
        boolean continuar = true;

        while (continuar) {
            int opcion = vUnidades.mostrarMenuPrincipal();
            switch (opcion) {
                case 1:
                    vUnidades.limpiarPantalla();
                    listarUnidadesEnTablero(bandoActual);
                    break;
                case 2:
                    vUnidades.limpiarPantalla();
                    listarUnidadesEnReserva(bandoActual);
                    break;
                case 3:
                    vUnidades.limpiarPantalla();
                    mostrarDetalleUnidad(bandoActual);
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        }
    }


    // Mostrar todas las unidades del jugador en tablero
    private void listarUnidadesEnTablero(Bando bando) {
        List<Unidad> unidades = juego.getUnidadesEnTablero(bando);
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades desplegadas actualmente.");
            return;
        }
        vUnidades.mostrarListaUnidades("Unidades desplegadas", unidades);
    }

    // Mostrar unidades en reserva
    private void listarUnidadesEnReserva(Bando bando) {
        List<Unidad> reserva = juego.getUnidadesEnReserva(bando);
        if (reserva.isEmpty()) {
            System.out.println("No hay unidades en reserva.");
            return;
        }
        vUnidades.mostrarListaUnidades("Unidades en reserva", reserva);
    }


    // Ver detalle completo de una unidad
    private void mostrarDetalleUnidad(Bando bando) {
        List<Unidad> unidades = juego.getUnidadesEnTablero(bando);
        Unidad seleccionada = vUnidades.seleccionarUnidadViva(unidades);
        if (seleccionada != null) {
            vUnidades.mostrarDetalleUnidad(seleccionada);
        }
    }
}