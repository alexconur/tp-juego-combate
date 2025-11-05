package org.vista;

public class Colores {

    // Código para resetear cualquier color y volver al default
    public static final String RESET = "\u001B[0m";

    // --- COLORES DE UNIDADES ---
    public static final String ALIADO = "\u001B[1;94m"; // Azul brillante
    public static final String ENEMIGO = "\u001B[1;91m"; // Rojo brillante
    public static final String ALIADO_OCULTO = "\u001B[2;94m"; // Azul tenue
    public static final String VACIO_U = "\u001B[1;90m"; // Gris (para el '·' de vacío)

    
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
}