package org.modelo.tablero.casillas;

import org.controlador.Colores;
import org.modelo.tablero.Casilla;

public class Bosque extends Casilla {
    private static final int BONUS_ATAQUE = 5;
    private static final int BONUS_MAGIA = 5;

    public static String codigo() { return "BO"; }

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
    public String getCodigoColorVista() { return Colores.TERRENO_BOSQUE_BG; }

    @Override
    public String descripcionEfecto() {
        return "Aumenta " + BONUS_ATAQUE + " ATK y " + BONUS_MAGIA + " MGC, permite emboscadas.";
    }
}
