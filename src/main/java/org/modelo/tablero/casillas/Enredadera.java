package org.modelo.tablero.casillas;

import org.controlador.Colores;
import org.modelo.tablero.Casilla;

public class Enredadera extends Casilla {

    public static String codigo() { return "EN"; }


    public Enredadera(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return false; }

    @Override
    public String getTipoTerreno() { return "Enredadera"; }

    @Override
    public String getCodigoColorVista() { return Colores.TERRENO_ENREDADERA_BG; }

    @Override
    public String descripcionEfecto() {
        return "No se puede recorrer.";
    }
}
