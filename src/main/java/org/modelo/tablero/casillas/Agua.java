package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;

public class Agua extends Casilla {

    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("AG", Agua::new);
    }

    public Agua(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return false; }

    @Override
    public String getTipoTerreno() { return "Agua"; }
    
    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_AGUA_BG; }
}
