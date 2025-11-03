package org.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.modelo.tablero.Casilla;
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
    private boolean gameOver = false;

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
        Bando bandoAnterior = (bandoActual == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;

        //  Preparar unidades del nuevo bando
        //  Incluye bonus de defensa si descansaron
        for (Unidad u : getUnidadesEnTablero(bandoActual)) {
            u.prepararParaNuevoTurno();  // <-- este método aplica el bonus defensivo
        }

        // Limpiar los bonus temporales del bando anterior
        for (Unidad u : getUnidadesEnTablero(bandoAnterior)) {
            u.resetearBonusTemporales();
        }

        // Aplicar efectos pasivos de posición
        aplicarBonosDePosicion(bandoActual);

        // Verificar fin de juego
        if (isGameOver()) {
            System.out.println("🏁 ¡La partida ha terminado!");
        }
    }

    private void prepararUnidadesFinTurno(Bando bando) {
        // *IMPORTANTE*: Llama a prepararParaNuevoTurno al FINAL del turno
        for (Unidad u : getUnidadesEnTablero(bando)) {
            if (!u.estaVivo()) continue;
            Casilla c = u.getCasillaActual();
            if (c != null) {
                c.aplicarEfectoFinDeTurno(u);
            }
        }
    }
    
    // private void prepararUnidadesNuevoTurno(Bando bando) {
    //     // *X*: Aquí iría lógica si algo debe aplicarse al INICIO del turno
    //     for (Unidad u : getUnidadesEnTablero(bando)) {
    //         if (u.estaVivo()) {
    //             u.prepararParaNuevoTurno(); // resetea acción/mov y limpia bonus temporales
    //         }
    //     }
    // }

    private void aplicarBonosDePosicion(Bando bando) {
        for (Unidad u : getUnidadesEnTablero(bando)) {
            if (!u.estaVivo()) continue;
            Casilla c = u.getCasillaActual();
            if (c != null) {
                c.aplicarEfectoDePosicion(u); // p.ej. Bosque +ATK/+MGC, etc.
            }
        }
    }
    
    // --- Lógica de fin de juego ---
     public boolean isGameOver() {
        if (gameOver) return true;

        boolean lord1Vivo = bando1EnTablero.stream().anyMatch(u -> u.isLord() && u.estaVivo());
        boolean lord2Vivo = bando2EnTablero.stream().anyMatch(u -> u.isLord() && u.estaVivo());

        if (!lord1Vivo) {
            System.out.println("🏁 ¡El " + Bando.REINO_NIGROMANTICO + " ha ganado!");
            gameOver = true;
            return true;
        }
        if (!lord2Vivo) {
            System.out.println("🏁 ¡El " + Bando.REINO_DRUIDA + " ha ganado!");
            gameOver = true;
            return true;
        }

        return false;
    }

    // --- Rendición ---
    public void rendirse(Bando bando) {
        if (gameOver) {
            System.out.println("El juego ya había terminado.");
            return;
        }

        System.out.println("😩 El " + bando + " se ha rendido.");
        Bando ganador = (bando == Bando.REINO_DRUIDA)
                ? Bando.REINO_NIGROMANTICO
                : Bando.REINO_DRUIDA;
        System.out.println("🏁 ¡El " + ganador + " ha ganado la partida por rendición!");
        gameOver = true;
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
