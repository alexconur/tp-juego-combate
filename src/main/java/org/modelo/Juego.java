package org.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;

public class Juego {
    private Tablero tablero;
    final private List<Unidad> bando1EnTablero;
    final private List<Unidad> bando2EnTablero;
    final private List<Unidad> bando1Reserva;
    final private List<Unidad> bando2Reserva;
    private final Random rng;
    private Bando bandoActual;
    private boolean gameOver = false;
    private int numeroTurno;

    public Juego() {
        this.tablero = null; // *X*: se reemplaza en ControladorInicio
        this.rng = new Random();
        this.bando1EnTablero = new ArrayList<>();
        this.bando1Reserva = new ArrayList<>();
        this.bando2EnTablero = new ArrayList<>();
        this.bando2Reserva = new ArrayList<>();
        this.bandoActual = Bando.random(this.rng);
    }

    public Tablero getTablero() { return tablero; }
    public Bando getBandoActual() { return bandoActual; }
    public int getNumeroTurno() { return numeroTurno; }
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
    public boolean desplegarUnidad(Unidad unidad, int fila, int columna) {
        if (unidad == null || tablero == null) return false;
        
        try {
            // *A* todo este bloque rompe mucho POLK y TDA
            Casilla destino = tablero.getCasilla(fila, columna);
            if (destino == null) {
                System.out.println("Error: Casilla (" + fila + "," + columna + ") no existe.");
                return false;
            }

            if (destino.estaOcupada()) {
                System.out.println("Error: Casilla (" + fila + "," + columna + ") ya está ocupada.");
                return false;
            }

            if (!unidad.isLord()){
                Unidad lord = getLordEnTablero(unidad.getBando());
                if (lord == null) {
                    System.out.println("Error: No se encontró al Lord del " + unidad.getBando() + ".");
                    return false;
                }
    
                Casilla casillaLord = lord.getCasillaActual(); 
                int radioPermitido = 1;

                if (!tablero.esAdyacente(casillaLord, destino, radioPermitido)) {
                    System.out.println("Error: La unidad debe desplegarse adyacente al Lord.");
                    return false;
                }
            }

            destino.ocupar(unidad);

            if (unidad.getBando() == Bando.REINO_DRUIDA) {
                bando1Reserva.remove(unidad);
                bando1EnTablero.add(unidad);
            } else {
                bando2Reserva.remove(unidad);
                bando2EnTablero.add(unidad);
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error al desplegar unidad ("+ unidad.getNombre() +") en (" + fila + "," + columna + "): " + e.getMessage());
            return false;
        }
    }

    // --- Getters para las listas --- 
    public List<Unidad> getUnidadesEnTablero(Bando bando) { return (bando == Bando.REINO_DRUIDA) ? bando1EnTablero : bando2EnTablero; }
    
    public List<Unidad> getUnidadesEnReserva(Bando bando) { return (bando == Bando.REINO_DRUIDA) ? bando1Reserva : bando2Reserva; }
    
    public List<Unidad> getTodasUnidadesEnReserva() {
        List<Unidad> todas = new ArrayList<>(bando1Reserva);
        todas.addAll(bando2Reserva);
        return todas;
    }

    public List<Unidad> getTodasUnidadesEnTablero() {
        List<Unidad> todas = new ArrayList<>(bando1EnTablero);
        todas.addAll(bando2EnTablero);
        return todas;
    }

    public void cambiarTurno() {

        aplicarEfectosFinDeTurno();

        prepararUnidadesFinTurno(bandoActual);

        // Cambiar el bando actual
        bandoActual = (bandoActual == Bando.REINO_DRUIDA)
                ? Bando.REINO_NIGROMANTICO
                : Bando.REINO_DRUIDA;

        this.numeroTurno++;

        // System.out.println("--- Turno del " + bandoActual + " ---");
        System.out.flush();

        // Preparar unidades del NUEVO bando
        for (Unidad u : getUnidadesEnTablero(bandoActual)) {
            u.prepararParaNuevoTurno();
        }

        // Efectos pasivos de posición que se aplican AL COMENZAR el turno
        aplicarBonosDePosicion(bandoActual);

        // Fin del juego
        if (isGameOver()) {
            System.out.println("🏁 ¡La partida ha terminado!");
        }
    }

    private Unidad getLordEnTablero(Bando bando) {
        List<Unidad> lista = getUnidadesEnTablero(bando);
        for (Unidad u : lista) {
            if (u.isLord()) return u;
        }
        return null;
    }


    private void aplicarEfectosFinDeTurno() {
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Casilla c = tablero.getCasilla(i, j);
                if (c != null) c.notificarFinDeTurno(bandoActual);   // *M* A chequear si va acá o en prepararUnidadesFinTurno, o si se puede hacer mejor
            }
        }
    }

    private void prepararUnidadesFinTurno(Bando bando) {
        // *IMPORTANTE*: Llama a prepararParaNuevoTurno al FINAL del turno
        // for (Unidad u : getUnidadesEnTablero(bando)) {
        //     if (!u.estaVivo()) continue;
        //     Casilla c = u.getCasillaActual();          *M* A chequear si esto lo tenemos que sacar o no
        //     if (c != null) {
        //         c.notificarFinDeTurno();                   
        //     }
        // }
    }

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
