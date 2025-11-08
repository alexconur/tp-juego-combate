package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.FabricaCasillas;

public class Bosque extends Casilla {
    private static final int BONUS_ATAQUE = 5;  // Valor hipotético
    private static final int BONUS_MAGIA = 5;  // Valor hipotético

    // *A* sé que static se ve feo pero no hay otra forma por ahora de no romper ocp
    static {
        FabricaCasillas.getInstancia().registrarTipoCasilla("BO", Bosque::new);
    }
    
    public Bosque(int fila, int columna) {
        super(fila, columna, null, null, (unidad) -> {
            unidad.aplicarBonusAtkTemporal(BONUS_ATAQUE);
            unidad.aplicarBonusMgcTemporal(BONUS_MAGIA);
        });
    }

    @Override
    public boolean esTransitable() { return true; }

    @Override
    public String getTipoTerreno() { return "Bosque"; }

    @Override    
    public boolean permiteEmboscada() { return true; }

    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_BOSQUE_BG; }
}
