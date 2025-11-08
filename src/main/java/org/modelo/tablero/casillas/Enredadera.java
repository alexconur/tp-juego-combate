package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;

public class Enredadera extends Casilla {

    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("EN", Enredadera::new);
    }

    public Enredadera(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return false; }

    @Override
    public String getTipoTerreno() { return "Enredadera"; }

    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_ENREDADERA_BG; }
}
