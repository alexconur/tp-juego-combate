package org.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;

public class Juego {
    // *A* falta lógica de que un jugador se pueda rendir.
    private Tablero tablero;
    private List<Unidad> bando1EnTablero;
    private List<Unidad> bando2EnTablero;
    private List<Unidad> bando1Reserva;
    private List<Unidad> bando2Reserva;
    private Bando bandoActual;

    //Constructor
    public Juego() {
        this.tablero = null; // *X*: se reemplaza en ControladorInicio
        this.bando1EnTablero = new ArrayList<>();
        this.bando1Reserva = new ArrayList<>();
        this.bando2EnTablero = new ArrayList<>();
        this.bando2Reserva = new ArrayList<>();

        
        Random random = new Random();
        this.bandoActual = random.nextBoolean() ? Bando.REINO_DRUIDA : Bando.REINO_NIGROMANTICO;  //*M* rompe OCP, vale la pena ??
        System.out.println("Inicia el turno: " + this.bandoActual);
    }


    // Cargar tablero
    // public void cargarTablero() {
    //     FabricaCasillas fabrica = new FabricaCasillas(); //rompe algo??? suspishus, idk

    //     for (int i = 0; i < tablero.getFilas(); i++) {
    //         for (int j = 0; j < tablero.getColumnas(); j++) {
    //             //*M* acá hacemos algo con lo que nos dan desde los archivos (creo que tenemos como min un parametro en este método)
    //         }
    //     }

    // }

    public Tablero getTablero() { return tablero; }
    public Bando getBandoActual() { return bandoActual; }
    public void reemplazarTablero(Tablero nuevo) { this.tablero = nuevo; }

    // Carga las unidades en las reservas
    public void setEjercitos(List<Unidad> unidades) {
        for (Unidad u : unidades) {
            if (u.getBando() == Bando.REINO_DRUIDA) {
                bando1Reserva.add(u);
            } else {
                bando2Reserva.add(u);
            }
        }
    }


    // agrego las unidades la lista
    public void desplegarUnidad(Unidad unidad, int fila, int columna) {
        if (unidad == null || tablero == null) return;
        
        try {
            // *CORRECCIÓN IMPORTANTE*: La casilla debe obtenerse del tablero
            if (tablero.getCasilla(fila, columna) == null) {
                 System.out.println("Error al desplegar: Casilla (" + fila + "," + columna + ") es nula.");
                 return;
            }
        
            tablero.getCasilla(fila, columna).ocupar(unidad);
            
            if (unidad.getBando() == Bando.REINO_DRUIDA) {
                bando1Reserva.remove(unidad);
                bando1EnTablero.add(unidad);
            } else {
                bando2Reserva.remove(unidad);
                bando2EnTablero.add(unidad);
            }
            
        } catch (Exception e) {
            System.out.println("Error al desplegar unidad ("+ unidad.getNombre() +") en (" + fila + "," + columna + "): " + e.getMessage());
        }
    }

    // --- Getters para las listas --- 
    public List<Unidad> getUnidadesEnTablero(Bando bando) { return (bando == Bando.REINO_DRUIDA) ? bando1EnTablero : bando2EnTablero; }
    
    public List<Unidad> getUnidadesEnReserva(Bando bando) { return (bando == Bando.REINO_DRUIDA) ? bando1Reserva : bando2Reserva; }
    
    public List<Unidad> getTodasUnidadesEnTablero() {
        List<Unidad> todas = new ArrayList<>(bando1EnTablero);
        todas.addAll(bando2EnTablero);
        return todas;
    }

    public void cambiarTurno() {
        // Prepara las unidades del bando actual ANTES de cambiar
        prepararUnidadesFinTurno(bandoActual);

        bandoActual = (bandoActual == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;
        System.out.println("--- Turno del " + bandoActual + " ---");

        // Prepara las unidades del nuevo bando para el inicio de su turno
        prepararUnidadesNuevoTurno(bandoActual);
    }

    private void prepararUnidadesFinTurno(Bando bando) {
        // *IMPORTANTE*: Llama a prepararParaNuevoTurno al FINAL del turno
        for (Unidad u : getUnidadesEnTablero(bando)) {
             if (u.estaVivo()) {
                u.prepararParaNuevoTurno(); 
            }
        }
    }
    
    private void prepararUnidadesNuevoTurno(Bando bando) {
        // *X*: Aquí iría lógica si algo debe aplicarse al INICIO del turno
    }
    
    // --- Lógica de fin de juego ---
    public boolean isGameOver() {
        // Filtra solo Lords VIVOS
        boolean lord1Vivo = bando1EnTablero.stream()
            .anyMatch(u -> u.isLord() && u.estaVivo());
            
        boolean lord2Vivo = bando2EnTablero.stream()
            .anyMatch(u -> u.isLord() && u.estaVivo());

        // Si bando1 NO tiene un lord vivo, bando2 gana
        if (!lord1Vivo) {
            System.out.println("¡El " + Bando.REINO_NIGROMANTICO + " ha ganado!");
            return true;
        }
        // Si bando2 NO tiene un lord vivo, bando1 gana
        if (!lord2Vivo) {
            System.out.println("¡El " + Bando.REINO_DRUIDA + " ha ganado!");
            return true;
        }
        
        return false;
    }
    
    // Encontrar al Lord de un bando (en reserva)
    public Unidad getLordDeReserva(Bando bando) {
        List<Unidad> reserva = getUnidadesEnReserva(bando);
        for (Unidad u : reserva) {
            if (u.isLord()) return u;
        }
        return null; // *X* No debería pasar si el CSV es correcto
    }

    // *M* manejar la visibilidad de las unidades (actualizarlas)
}
