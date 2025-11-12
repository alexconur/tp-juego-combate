package org.modelo.tablero.casillas;

import org.controlador.Colores;
import org.modelo.tablero.Casilla;

public class Llanura extends Casilla {
    public static String codigo() { return "LL"; }

    public Llanura(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return true; }
    
    @Override
    public String getTipoTerreno() { return "Llanura"; }

    @Override
    public String getCodigoColorVista() { return Colores.TERRENO_LLANURA_BG; }

    @Override
    public String descripcionEfecto() {
        return "Terreno abierto.";
    }
}
