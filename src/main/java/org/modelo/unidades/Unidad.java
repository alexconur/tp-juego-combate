package org.modelo.unidades;

import org.modelo.equipamiento.Equipamiento;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero; 

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
    private boolean descansoTurno;                       
    private static final int BONUS_DEF_DESCANSO = 3;

    private Casilla casillaActual; // Logica tablero

    private boolean ataqueRealizado;
    private int movimientoRestante;

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
        this.movimientoRestante = mov;
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
    public boolean estaVivo(){ return hp > 0; } // *A* para eliminar del juego a la unidad
    public boolean isOculto() { return oculto; }
    public boolean isLord() { return esLord; }

    public int getMovimientoRestante(){ return movimientoRestante; }
    public boolean puedeMoverse(){ return movimientoRestante > 0 && !ataqueRealizado ;}
    public boolean puedeActuar(){ return !ataqueRealizado;}

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
    public void setMovimientoRestante(int mov) { this.movimientoRestante = mov; }

    // Metodos al recibir daño o cura
    public void recibirDanio(int cantDanio){
        this.hp -= cantDanio;
        if (this.hp < 0){
            this.hp = 0;
        }
        // Se pierde el bonus defensivo
        if (this.bonusDefTemporal > 0) {
            System.out.println(nombre + " pierde su bonus defensivo al ser atacada!");
            this.resetearBonusTemporales();
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
        // Si descansó, aplica bonus defensivo
        if (this.descansoTurno) {
            this.aplicarBonusTemporal("DEF", BONUS_DEF_DESCANSO);
            System.out.println(nombre + " recibe +" + BONUS_DEF_DESCANSO + " DEF por descansar el turno anterior.");
        } else {
            // Si no descansó, limpia cualquier bonus anterior
            this.resetearBonusTemporales();
        }

        // Resetear acciones para el nuevo turno
        this.ataqueRealizado = false;
        this.movimientoRestante = this.mov;

        // Ahora vuelve a marcarse como descansando,
        // y perderá ese estado si ataca o se mueve
        this.descansoTurno = true;
    }


    // --- ACCIONES DE LA UNIDAD ---
    public boolean yaAtaco() { return ataqueRealizado; }
    public boolean yaSeMovio() { return movimientoRestante < mov; }

    // Ataca a una unidad objetivo. Delega la lógica de cálculo de daño al equipamiento (Strategy).
        // Si no tiene equipamiento o está roto, ataca "a puño limpio".
    public void atacar(Unidad objetivo) {
        if (!puedeActuar() || this.bando == objetivo.getBando()) {
            return;
        }
        
        // Lógica de Puño Limpio (Rango 1)
        if (equipamiento == null || !equipamiento.esOfensivo() || equipamiento.estaRoto()) {
            // Solo puede atacar adyacente
            if (this.casillaActual.getFila() - objetivo.getCasillaActual().getFila() > 1 ||
                this.casillaActual.getColumna() - objetivo.getCasillaActual().getColumna() > 1) {
                System.out.println("El objetivo está fuera de rango para atacar a puño limpio.");
                return;
            }
            int danio = this.getAtkTotal() - objetivo.getDef();
            objetivo.recibirDanio(Math.max(0, danio));
        } else {
            // Chequeo de rango del equipamiento
            int dist = Math.max(Math.abs(this.casillaActual.getFila() - objetivo.getCasillaActual().getFila()),
                                Math.abs(this.casillaActual.getColumna() - objetivo.getCasillaActual().getColumna()));
            
            if (dist > equipamiento.getRango()) {
                 System.out.println("El objetivo está fuera de rango del equipamiento (Rango: " + equipamiento.getRango() + ", Dist: " + dist + ")");
                 return;
            }
            
            equipamiento.accionar(this, objetivo);
        }
        
        this.ataqueRealizado = true;
        this.movimientoRestante = 0; // Atacar consume el movimiento
        this.revelar();
        this.descansoTurno = false;          // no descansó
        this.resetearBonusTemporales();      // pierde bonus temporales al atacar
    }

    // Cura a una unidad aliada. Solo funciona si tiene un Báculo equipado.
    public void curarAliado(Unidad aliado) {
        if (!puedeActuar() || this.bando != aliado.getBando()) {
            return;
        }

        if (equipamiento != null && !equipamiento.esOfensivo() && !equipamiento.estaRoto()) {
            // Delega la acción de curar al báculo
            equipamiento.accionar(this, aliado);
            this.ataqueRealizado = true; // Curar cuenta como la acción del turno
            this.revelar();
            this.descansoTurno = false;      // curar también cancela el descanso
            this.resetearBonusTemporales();
        }
    }

    // Emboscada -------------
    public boolean puedePrepararEmboscada() {
        return !this.oculto && !this.yaAtaco() && !this.yaSeMovio();
    }

    public void prepararEmboscada() {
        if (puedePrepararEmboscada()) {
            this.oculto = true;
            System.out.println(nombre + " se oculta entre los árboles...");
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
    public void moverA(Tablero tablero, int nuevaFila, int nuevaColumna) {
        if (!puedeMoverse()) {
            System.out.println(nombre + " no puede moverse (ya actuó o no tiene movimiento).");
            return;
        }

        // Validación simple de distancia: permite 8 direcciones 
        int dist = Math.max(
            Math.abs(casillaActual.getFila() - nuevaFila), 
            Math.abs(casillaActual.getColumna() - nuevaColumna)
        );

        if (dist == 0) {
            System.out.println("No te puedes mover a la casilla actual.");
            return;
        }

        if (dist > this.movimientoRestante) {
            System.out.println("Movimiento inválido: distancia (" + dist + 
                            ") excede el movimiento restante (" + this.movimientoRestante + ").");
            return;
        }

        try {
            // Tablero se encarga de validar el movimiento y de realizarlo
            tablero.moverUnidad(this, nuevaFila, nuevaColumna);

            // Revelar la unidad (por si estaba oculta)
            revelar();

            // 🟡 Si tenía bonus defensivo (por haber descansado), lo pierde al moverse
            if (this.bonusDefTemporal > 0) {
                System.out.println(nombre + " pierde su bonus defensivo al moverse.");
                this.resetearBonusTemporales();
            }

            // Ya no se considera que descansó
            this.descansoTurno = false;
            this.movimientoRestante = 0;

            System.out.println("✅ " + nombre + " se movió a (" + nuevaFila + "," + nuevaColumna + ").");

        } catch (org.modelo.tablero.excepciones.CasillaOcupadaException e) {
            System.out.println("Casilla ocupada: " + e.getMessage());
        } catch (org.modelo.tablero.excepciones.CasillaIntransitableException e) {
            System.out.println("Movimiento inválido: " + e.getMessage());
        }
    }
}