package org.modelo.tablero.casillas;

import org.modelo.tablero.Casilla;

public class AreaContaminada extends Casilla {
    private static final int HP_DISMINUIDO = 5;  // Valor hipotético

    public static String codigo() { return "AR"; }

    public AreaContaminada(int fila, int columna) {
        super(fila, columna, null,
      (unidad, casilla, bandoActual) -> {
          if (unidad.getBando() == bandoActual) {
              unidad.recibirDanio(HP_DISMINUIDO);
              // System.out.println(unidad.getNombre() + " recibió daño " + HP_DISMINUIDO + " en Área Contaminada.");
          }
      },
      null);
    }

    @Override
    public boolean esTransitable() { return true; }

    @Override
    public String getTipoTerreno() { return "Area Contaminada"; }

    @Override
    public String getCodigoColorVista() { return org.vista.Colores.TERRENO_PELIGROSO_BG; }

    @Override
    public String descripcionEfecto() {
        return "Reduce " + HP_DISMINUIDO + " HP al final del turno.";
    }
}
