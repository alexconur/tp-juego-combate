package org.main;

import org.controlador.ControladorPrincipal;
import org.modelo.Juego;
import org.vista.VistaPrincipal;

public class Main {
    public static void main(String[] args) {
        // *I* main no debería necesitar más que esto para el resto del proyecto

        //*M* necesitamos el parseo de los archivos para cargar los datos iniciales
        Juego juego = new Juego(1,1); // *A* puse valores temporales, despues en ControladorInicio se reemplaza el tablero
        VistaPrincipal vista = new VistaPrincipal();
        ControladorPrincipal controlador = new ControladorPrincipal(juego, vista);

        controlador.ejecutar();
    }
}