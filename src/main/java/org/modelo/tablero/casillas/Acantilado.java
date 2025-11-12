package org.modelo.tablero.casillas;

import org.controlador.Colores;
import org.modelo.tablero.Casilla;
public class Acantilado extends Casilla {

    public static String codigo() { return "AC"; }

    public Acantilado(int fila, int columna) {
        super(fila, columna, null, null, null);
    }

    @Override
    public boolean esTransitable() { return false; }

    @Override
    public String getTipoTerreno() { return "Acantilado"; }

    @Override
    public String getCodigoColorVista() { return Colores.TERRENO_ACANTILADO_BG; }

    @Override
    public String descripcionEfecto() {
        return "No se puede avanzar.";
    }
}
