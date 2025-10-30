package org.vista.tipos;

import org.vista.Vista;

public class VistaInicio implements Vista {
    // indica a usuario como iniciar el juego
    public void mostrar() {
        System.out.println("Iniciando juego...");
        System.out.println("| Bienvenido a CLASS EMBLEM |\n");
    }

    // *I* esquema de vista para iniciar partida, si bien están colocados como
    // voids, estos métodos deberían devolver la info seleccionada por el usuario
    // para que el controlador la procese y actualice el modelo.
    public void pedirMapa(){
        System.out.println("Seleccione mapa inicial: ");
        // se obtiene y llama a lógica de seleccion de mapa (parseo archivo y demás)
        // *se encarga controlador*
    }

    public void pedirEjercito(){
        System.out.println("Seleccione su ejercito: ");
        // "" seleccion de ejercito (parseo archivo y demás)
        // *se encarga controlador*
    }

    public void pedirUbicarLord(){
        System.out.println("Ubique su lord: ");
        // "" de ubicacion de lord en tablero
        // *se encarga controlador* -> chequeo de casilla válida y demás.
    }
}
