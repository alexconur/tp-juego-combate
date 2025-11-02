package org.main;

import org.controlador.ControladorPrincipal;
import org.modelo.Juego;
import org.vista.VistaPrincipal;

public class Main {
    public static void main(String[] args) {
        // *I* main no debería necesitar más que esto para el resto del proyecto

        //*M* necesitamos el parseo de los archivos para cargar los datos iniciales
        Juego juego = new Juego(); // *X* corregi lo que puso aylu de valores iniciales
        VistaPrincipal vista = new VistaPrincipal();
        ControladorPrincipal controlador = new ControladorPrincipal(juego, vista);

        controlador.ejecutar();
    }
}