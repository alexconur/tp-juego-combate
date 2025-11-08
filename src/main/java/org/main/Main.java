package org.main;

import org.controlador.ControladorPrincipal;
import org.modelo.Juego;
import org.vista.VistaPrincipal;

public class Main {
    public static void main(String[] args) {        
        Juego juego = new Juego();
        VistaPrincipal vista = new VistaPrincipal();
        ControladorPrincipal controlador = new ControladorPrincipal(juego, vista);

        controlador.ejecutar();
    }
}