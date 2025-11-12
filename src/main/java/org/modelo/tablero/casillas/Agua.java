package org.modelo.tablero.casillas;

import org.controlador.Colores;
import org.modelo.tablero.Casilla;

public class Agua extends Casilla {

    public static String codigo() { return "AG"; }

    public Agua(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return false; }

    @Override
    public String getTipoTerreno() { return "Agua"; }
    
    @Override
    public String getCodigoColorVista() { return Colores.TERRENO_AGUA_BG; }

    @Override
    public String descripcionEfecto() {
        return "No es transitable.";
    }
}
