package org.modelo.unidades;

import org.modelo.equipamiento.Equipamiento;

public class Unidad {
    
    // Atributos
    private String nombre;
    private int hp, atk, def, mgc, mov;
    // private boolean ataqueRealizado, movimientoRealizado; // *A* creo que esto esta de mas
    private boolean oculto;
    private Equipamiento equipamiento;
    private Bando bando;
    private boolean esLord;

    // Constructor
    public Unidad(String nombre, int hp, int atk, int def, int mgc, int mov, Bando bando) {
        this.nombre=nombre;
        this.hp=hp;
        this.atk=atk;
        this.def=def;
        this.mgc=mgc;
        this.mov=mov;
        this.bando=bando;
        this.oculto = false;
        this.equipamiento = null;
        this.esLord = false;
    }

    // Getters

    public Bando getBando() {
        return bando;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHp() {
        return hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getMgc() {
        return mgc;
    }

    public int getMov() {
        return mov;
    }

    // Metodos posibles: mover, atacar, ...
    public boolean estaVivo(){
        return hp > 0;
    }

    public boolean isOculto() {
        return oculto;
    }

    public void recibirDanio(int cantDanio){
        hp -= cantDanio;
        if (hp<0){
            hp=0;
        }
    }

    public void curar(int cantCurar) {
        hp += cantCurar;
    }

    public Equipamiento getEquipamiento() {
        return equipamiento;
    }


    // *X*: Agregados para que no de error


    public void setOculto(boolean oculto) {
        this.oculto = oculto;
    }

    public boolean isEsLord() {
        return esLord;
    }

    public void setEsLord(boolean esLord) {
        this.esLord = esLord;
    }

    public void setEquipamiento(Equipamiento equipamiento) {
        this.equipamiento = equipamiento;
    }
}