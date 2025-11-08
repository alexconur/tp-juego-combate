package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;

public class Pantano extends Casilla {

    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("PA", Pantano::new);
    }

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
}
