package org.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;

public class Juego {
    // *A* falta lógica de que un jugador se pueda rendir ademas de perder, y termine el juego.
    private Tablero tablero;
    private List<Unidad> bando1;
    private List<Unidad> bando2;
    private Bando bandoActual;

    //Constructor
    public Juego(int filas, int columnas) {
        this.tablero = new Tablero(filas, columnas);
        this.bando1 = new ArrayList<>();
        this.bando2 = new ArrayList<>();
        
        Random random = new Random();
        this.bandoActual = random.nextBoolean() ? Bando.REINO_DRUIDA : Bando.REINO_NIGROMANTICO;  //*M* rompe OCP, vale la pena ??
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

    public Tablero getTablero() {
        return tablero;
    }

    public void reemplazarTablero(Tablero nuevo) {
        this.tablero = nuevo;
    }

    // agrego las unidades la lista
    public void agregarUnidades(Unidad unidad, int fila, int columna) {
        tablero.getCasilla(fila, columna).ocupar(unidad);  //*M* POLK ???????????
        unidad.setCasillaActual(tablero.getCasilla(fila, columna));

        if (unidad.getBando() == Bando.REINO_DRUIDA) { //*M* TDA ????????
            bando1.add(unidad);
        } else {
            bando2.add(unidad);
        }

    }

    public void cambiarTurno() {
        bandoActual = (bandoActual == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;    // *M* OCP ?????????                             
        System.out.println("Turno del " + bandoActual);
        prepararUnidades();
    }

    private void prepararUnidades() {
        List<Unidad> todas = new ArrayList<>();
        todas.addAll(bando1);
        todas.addAll(bando2);
        for (Unidad u : todas) {
            u.prepararParaNuevoTurno();
        }
    }

    // *M* manejar la visibilidad de las unidades (actualizarlas)
    // Ver quien gana
    // juego terminado

    
}
