package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;

public class Acantilado extends Casilla {

    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("AC", Acantilado::new);
    }

    public Acantilado(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return false; }

    @Override
    public String getTipoTerreno() { return "Acantilado"; }

    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_ACANTILADO_BG; }
}
