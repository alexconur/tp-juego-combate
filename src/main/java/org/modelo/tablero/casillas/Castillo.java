package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class Castillo extends Casilla {
    private static final int CURACION_FUERTE = 10; // Valor hipotético
    private static final int BONUS_DEFENSA = 5;  // Valor hipotético

    public static String codigo() { return "FO"; }

    public Castillo(int fila, int columna) {
        super(fila, columna, null, (unidad, casilla) -> {
                  unidad.recibirCuracion(CURACION_FUERTE);
                  unidad.aplicarBonusDefTemporal(BONUS_DEFENSA);
                  System.out.println(unidad.getNombre() + " se cura " + CURACION_FUERTE + " HP en el fuerte.");
              }, null);
    }

    @Override
    public boolean esTransitable() { return true; }

    @Override
    public String getTipoTerreno() { return "Castillo"; }

    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_CASTILLO_BG; }
}
