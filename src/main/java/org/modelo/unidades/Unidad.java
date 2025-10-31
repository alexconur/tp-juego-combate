package org.modelo.unidades;

import org.modelo.equipamiento.Equipamiento;
import org.modelo.tablero.Casilla;

public class Unidad {
    
    // Atributos
    private String nombre;
    private int hp, maxHp, atk, def, mgc, mov;
    private boolean oculto;
    private Equipamiento equipamiento;
    private Bando bando;
    private boolean esLord;

    // Atributos para buffs temporales
    private int bonusAtkTemporal;
    private int bonusDefTemporal;
    private int bonusMgcTemporal;

    private Casilla casillaActual; // Logica tablero

    private boolean ataqueRealizado, movimientoRealizado; // Logica turnos

    // Constructor
    public Unidad(String nombre, int hp, int atk, int def, int mgc, int mov, Bando bando) {
        this.nombre=nombre;
        this.hp=hp;
        this.maxHp = hp;
        this.atk=atk;
        this.def=def;
        this.mgc=mgc;
        this.mov=mov;
        this.bando=bando;
        this.oculto = false;
        this.equipamiento = null;
        this.esLord = false;

        // --> Las unidades empiezan listas para su primer turno
        this.ataqueRealizado = false;
        this.movimientoRealizado = false;
    }

    // Getters de Estado
    public Bando getBando() { return bando; }
    public String getNombre() { return nombre; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMov() { return mov; }
    public int getDef() { return this.def + this.bonusDefTemporal; }
    public Casilla getCasillaActual(){ return casillaActual; }
    public Equipamiento getEquipamiento(){ return equipamiento; }
    public boolean estaVivo(){ return hp > 0; }
    public boolean isOculto() { return oculto; }
    public boolean isLord() { return esLord; }

    // Devuelve el ATK total (base + bonus de equipamiento).
    public int getAtkTotal() {
        int total = this.atk + this.bonusAtkTemporal;
        if (equipamiento != null && !equipamiento.estaRoto()) {
            return total + equipamiento.getBonusAtaque();
        }
        return total;
    }

    //Devuelve el MGC total (base + bonus de equipamiento).
    public int getMgcTotal() {
        int total = this.mgc + this.bonusMgcTemporal;
        if (equipamiento != null && !equipamiento.estaRoto()) {
            return total + equipamiento.getBonusMagia();
        }
        return total;
    }

    // Setters
    public void setEquipamiento(Equipamiento equipamiento) { this.equipamiento = equipamiento; }
    public void setCasillaActual(Casilla casilla) { this.casillaActual = casilla; }
    public void setEsLord(boolean esLord) { this.esLord = esLord; }
    public void setOculto(boolean oculto) { this.oculto = oculto; } // *X*: lo habia puesto xq a nacho le daba error, pero a mi no me dio ese error...


    // Metodos al recibir daño o cura
    public void recibirDanio(int cantDanio){
        this.hp -= cantDanio;
        if (this.hp < 0){
            this.hp = 0;
        }
    }

    public void recibirCuracion(int cantCurar) {
        this.hp += cantCurar;
        if (this.hp > this.maxHp) { // No puede curar más de la vida máxima
            this.hp = this.maxHp;
        }
    }
    // Revela la unidad si estaba oculta. Se llama al moverse o atacar.
    public void revelar() {
        if (this.oculto) {
            this.oculto = false;
        }
    }

    // Resetea los flags de acción al inicio de un nuevo turno.
    public void prepararParaNuevoTurno() {
        this.ataqueRealizado = false;
        this.movimientoRealizado = false;
        this.resetearBonusTemporales();
        // Aquí también se aplicarían efectos de terreno (Fuerte, Área Contaminada)
    }


    // --- ACCIONES DE LA UNIDAD ---
    public void setMovimientoRealizado(boolean movimientoRealizado) { this.movimientoRealizado = movimientoRealizado; }
    public boolean yaAtaco() { return ataqueRealizado; }
    public boolean yaSeMovio() { return movimientoRealizado; }

    // Ataca a una unidad objetivo. Delega la lógica de cálculo de daño al equipamiento (Strategy).
        // Si no tiene equipamiento o está roto, ataca "a puño limpio".
    public void atacar(Unidad objetivo) {
        if (ataqueRealizado || this.bando == objetivo.getBando()) {
            // Ya atacó este turno o está intentando atacar a un aliado
            return;
        }
        if (equipamiento == null || !equipamiento.esOfensivo() || equipamiento.estaRoto()) {
            // Lógica de "Puño Limpio"
            int danio = this.getAtkTotal() - objetivo.getDef(); // Usa AtkTotal (base)
            objetivo.recibirDanio(Math.max(0, danio)); // Daño físico
        } else {
            // Delegar la acción al equipamiento (Strategy)
            equipamiento.accionar(this, objetivo);
        }
        this.ataqueRealizado = true;
        this.revelar(); // Atacar revela la unidad
    }

    // Cura a una unidad aliada. Solo funciona si tiene un Báculo equipado.
    public void curarAliado(Unidad aliado) {
        if (ataqueRealizado || this.bando != aliado.getBando()) {
            // Ya actuó o está intentando curar a un enemigo
            return;
        }

        if (equipamiento != null && !equipamiento.esOfensivo() && !equipamiento.estaRoto()) {
            // Delega la acción de curar al báculo
            equipamiento.accionar(this, aliado);
            this.ataqueRealizado = true; // Curar cuenta como la acción del turno [cite: 69]
        }
    }

    // --- Metodos para aplicar y resetear los bonus ---
    // *X*: Cambiar switch
    public void aplicarBonusTemporal(String stat, int valor) {
        switch (stat) {
            case "ATK": this.bonusAtkTemporal += valor; break;
            case "DEF": this.bonusDefTemporal += valor; break;
            case "MGC": this.bonusMgcTemporal += valor; break;
        }
    }

    public void resetearBonusTemporales() {
        this.bonusAtkTemporal = 0;
        this.bonusDefTemporal = 0;
        this.bonusMgcTemporal = 0;
    }

    // --- MOVIMIENTO EN EL TABLERO ---
    public void moverA(org.modelo.tablero.Tablero tablero, int nuevaFila, int nuevaColumna) {
        if (movimientoRealizado) {
            System.out.println(nombre + " ya se movió este turno.");
            return;
        }

        try {
            // Tablero se encarga de validar el movimiento y de realizarlo
            tablero.moverUnidad(this, nuevaFila, nuevaColumna);

            // Seteo al movimiento como realizado y revelo la unidad
            movimientoRealizado = true;
            revelar();

            System.out.println(nombre + " se movió a (" + nuevaFila + "," + nuevaColumna + ").");
        } catch (org.modelo.tablero.excepciones.CasillaOcupadaException e) {
            System.out.println("Casilla ocupada: " + e.getMessage());
        } catch (org.modelo.tablero.excepciones.CasillaIntransitableException e) {
            System.out.println("Movimiento inválido: " + e.getMessage());
        }
    }
}