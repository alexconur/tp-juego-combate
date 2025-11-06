package org.vista;

import org.modelo.unidades.Bando;

public class Colores {

    // Código para resetear cualquier color y volver al default
    public static final String RESET = "\u001B[0m";

    public static final String WARNING = "\u001B[38;5;208m\"";

    // --- COLORES DE UNIDADES ---
    public static final String VACIO_U = "\u001B[1;90m"; // Gris (para el '·' de vacío)
    public static final String DRUIDA = "\u001B[1;94m"; // Azul brillante
    public static final String DRUIDA_OCULTO = "\u001B[2;94m"; // Azul tenue
    public static final String NIGROMANTICO = "\u001B[1;91m"; // Rojo brillante
    public static final String NIGROMANTICO_OCULTO = "\u001B[2;91m"; // Rojo tenue


    // --- COLOR TERRENO TRANSITABLE ---
    public static final String TERRENO_ALCANZABLE_BG = "\u001B[103m"; // Fondo Amarillo brillante
    
    // --- COLORES DE TERRENOS ---
    public static final String TERRENO_LLANURA_BG = "\u001B[48;5;118m"; // Verde brillante
    public static final String TERRENO_BOSQUE_BG = "\u001B[48;5;28m";  // Verde
    public static final String TERRENO_PANTANO_BG = "\u001B[48;2;90;80;30m"; // Marrón/Amarillo
    public static final String TERRENO_CASTILLO_BG = "\u001B[100m"; // Gris brillante (Fuerte)
    public static final String TERRENO_AGUA_BG = "\u001B[44m";  // Azul
    public static final String TERRENO_PELIGROSO_BG = "\u001B[45m"; // Magenta (AreaContaminada)
    public static final String TERRENO_ACANTILADO_BG = "\u001B[40m"; // Negro (Acantilado, Enredadera)
    public static final String TERRENO_ENREDADERA_BG = "\u001B[48;2;128;150;0m";
    public static final String TERRENO_DEFAULT_BG = "\u001B[40m";

    public static String colorParaBando(Bando bando) {
        if (bando == null) return VACIO_U;
        return (bando == Bando.REINO_DRUIDA) ? DRUIDA : NIGROMANTICO;
    }

    public static String colorOcultoParaBando(Bando bando) {
        if (bando == null) return VACIO_U;
        return (bando == Bando.REINO_DRUIDA) ? DRUIDA_OCULTO : NIGROMANTICO_OCULTO;
    }
}