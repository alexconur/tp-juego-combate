package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;

public class Llanura extends Casilla {
    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("LL", Llanura::new);
    }

    public Llanura(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return true; }
    
    @Override
    public String getTipoTerreno() { return "Llanura"; }

    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_LLANURA_BG; }
}
