package org.modelo.unidades;

public class Lord extends Unidad {
    private static final int HP_DEFAULT  = 40;
    private static final int ATK_DEFAULT = 12; 
    private static final int DEF_DEFAULT = 9;
    private static final int MGC_DEFAULT = 6;
    private static final int MOV_DEFAULT = 6;

    private Lord(String nombre, int hp, int atk, int def, int mgc, int mov, Bando bando) {
        super(nombre, hp, atk, def, mgc, mov, bando);
        this.setEsLord(true);
    }

    // *A* hice valores por defecto porque no sé a que se refieren con que lord tenga stats mejorados
    public static Lord crear(String nombre, Bando bando) {
        return new Lord(nombre, HP_DEFAULT, ATK_DEFAULT, DEF_DEFAULT, MGC_DEFAULT, MOV_DEFAULT, bando);
    }

    // *A* factory para crearlo personalizado pero no se cual van a querer , PREGUNTAR AL PROFE!
    public static Lord crearPersonalizado(String nombre, int hp, int atk, int def, int mgc, int mov, Bando bando) {
        return new Lord(nombre, hp, atk, def, mgc, mov, bando);
    }
}
