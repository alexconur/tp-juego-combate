package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class Pantano extends Casilla {

    public static String codigo() { return "PA"; }

    public Pantano(int fila, int columna) {
        super(fila, columna, null,null, 
        (unidad) -> {
            unidad.setMovimientoRestante(1);
            System.out.println(unidad.getNombre() + " se atasca en el pantano!");
        });
    }

    @Override
    public boolean esTransitable() { return true; }
    
    @Override
    public String getTipoTerreno() { return "Pantano"; }

    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_PANTANO_BG; }

    @Override
    public String descripcionEfecto() {
        return "Reduce movimiento al minimo.";
    }
}
