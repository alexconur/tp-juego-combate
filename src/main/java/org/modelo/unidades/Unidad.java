package org.modelo.unidades;

import org.modelo.equipamiento.Equipamiento;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;

public abstract class Unidad {
    private String nombre;
    private int hp, maxHp, atk, def, mgc, mov;
    private boolean oculto;
    private Equipamiento equipamiento;
    private Bando bando;
    private boolean esLord;
    private boolean haSidoRevelada = false;
    private int bonusAtkTemporal;
    private int bonusDefTemporal;
    private int bonusMgcTemporal;
    private boolean descansoTurno;                       
    private static final int BONUS_DEF_DESCANSO = 3;
    private Casilla casillaActual;
    private boolean ataqueRealizado;
    private int movimientoRestante;

    protected Unidad(String nombre, int hp, int atk, int def, int mgc, int mov, Bando bando) {
        this.nombre=nombre;
        this.hp=hp;
        this.maxHp = hp;
        this.atk=atk;
        this.def=def;
        this.mgc=mgc;
        this.mov=mov;
        this.bando=bando;
        this.oculto = false;
        this.esLord = false;
        this.ataqueRealizado = false;
        this.movimientoRestante = mov;
    }

    public Bando getBando() { return bando; }
    public String getNombre() { return nombre; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMov() { return mov; }
    public int getDef() { return this.def + this.bonusDefTemporal; }
    public Casilla getCasillaActual(){ return casillaActual; }
    public Equipamiento getEquipamiento(){ return equipamiento; }
    public int getMovimientoRestante(){ return movimientoRestante; }

    public void setEquipamiento(Equipamiento equipamiento) { this.equipamiento = equipamiento; }
    public void setCasillaActual(Casilla casilla) { this.casillaActual = casilla; }
    protected void setEsLord(boolean esLord) { this.esLord = esLord; }
    public void setOculto(boolean oculto) { this.oculto = oculto; }
    public void setMovimientoRestante(int mov) { this.movimientoRestante = mov; }

    public boolean estaVivo(){ return hp > 0; }
    public boolean isOculto() { return oculto; }
    public boolean isLord() { return esLord; }
    public boolean puedeMoverse(){ return movimientoRestante > 0 && !ataqueRealizado ;}
    public boolean puedeActuar(){ return !ataqueRealizado;}
    public boolean yaAtaco() { return ataqueRealizado; }
    public boolean yaSeMovio() { return movimientoRestante < mov; }
    public boolean puedePrepararEmboscada() { return !this.haSidoRevelada && !this.oculto && !this.yaAtaco() && !this.yaSeMovio(); }

    public void aplicarBonusAtkTemporal(int valor) { this.bonusAtkTemporal += valor; }
    public void aplicarBonusDefTemporal(int valor) { this.bonusDefTemporal += valor; }
    public void aplicarBonusMgcTemporal(int valor) { this.bonusMgcTemporal += valor; }

    public int getAtkTotal() {
        int total = this.atk + this.bonusAtkTemporal;
        if (equipamiento != null && !equipamiento.estaRoto()) {
            return total + equipamiento.getBonusAtaque();
        }
        return total;
    }

    public int getMgcTotal() {
        int total = this.mgc + this.bonusMgcTemporal;
        if (equipamiento != null && !equipamiento.estaRoto()) {
            return total + equipamiento.getBonusMagia();
        }
        return total;
    }

    public void recibirDanio(int cantDanio){
        this.hp -= cantDanio;
        if (this.hp < 0){
            this.hp = 0;
        }
        if (this.bonusDefTemporal > 0) {
            this.resetearBonusTemporales();
        }

        if (!estaVivo()) {
            this.casillaActual.desocupar(); 
        }
    }

    public void recibirCuracion(int cantCurar) {
        this.hp += cantCurar;
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
    }

    public void revelar() {
        if (this.oculto) {
            this.oculto = false;
            this.haSidoRevelada = true; 
        }
    }

    public int distanciaA(Unidad objetivo) {
        if (this.casillaActual == null || objetivo == null || objetivo.getCasillaActual() == null) {
            return Integer.MAX_VALUE;
        }
        Casilla c1 = this.casillaActual;
        Casilla c2 = objetivo.getCasillaActual();
        return Math.max(Math.abs(c1.getFila() - c2.getFila()), Math.abs(c1.getColumna() - c2.getColumna()));
    }

    public void prepararParaNuevoTurno() {
        this.resetearBonusTemporales();

        if (this.descansoTurno) {
            this.aplicarBonusDefTemporal(BONUS_DEF_DESCANSO);
        }

        this.ataqueRealizado = false;
        this.movimientoRestante = this.mov;
        this.descansoTurno = true;
    }

    public void atacar(Unidad objetivo) {
        if (!puedeActuar() || this.bando == objetivo.getBando()) {
            return;
        }

        if (equipamiento == null || !equipamiento.esOfensivo() || equipamiento.estaRoto()) {
            int dist = this.distanciaA(objetivo);

            if (dist > 1) {
                System.out.println("El objetivo está fuera de rango para atacar a puño limpio.");
                return;
            }
            
            int danio = this.getAtkTotal() - objetivo.getDef();
            objetivo.recibirDanio(Math.max(0, danio));
        } else {
            int dist = this.distanciaA(objetivo);
            if (dist > equipamiento.getRango()) {
                System.out.println("El objetivo está fuera de rango del equipamiento (Rango: " + equipamiento.getRango() + ", Dist: " + dist + ")");
                return;
            }
            equipamiento.accionar(this, objetivo);
        }
        
        this.ataqueRealizado = true;
        this.movimientoRestante = 0;
        this.revelar();
        this.descansoTurno = false;
        this.resetearBonusTemporales();
    }

    public void curarAliado(Unidad aliado) {
        if (!puedeActuar() || this.bando != aliado.getBando()) {
            return;
        }

        if (!equipamiento.esOfensivo() && !equipamiento.estaRoto()) {
            equipamiento.accionar(this, aliado);
            this.ataqueRealizado = true; 
            this.revelar();
            this.descansoTurno = false;
            this.resetearBonusTemporales();
        }
    }

    public void prepararEmboscada() {
        if (puedePrepararEmboscada()) {
            this.oculto = true;
            System.out.println(nombre + " se oculta entre los árboles...");
        }
    }


    public void resetearBonusTemporales() {
        this.bonusAtkTemporal = 0;
        this.bonusDefTemporal = 0;
        this.bonusMgcTemporal = 0;
    }

    public void moverA(Tablero tablero, int nuevaFila, int nuevaColumna) {
        if (!puedeMoverse()) {
            System.out.println(nombre + " no puede moverse (ya actuó o no tiene movimiento).");
            return;
        }

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
        
        tablero.moverUnidad(this, nuevaFila, nuevaColumna);

        revelar();
        this.descansoTurno = false;

        if (this.bonusDefTemporal > 0) {
            System.out.println(nombre + " pierde su bonus defensivo al moverse.");
            this.resetearBonusTemporales();
        }

        this.movimientoRestante = 0;
    }
}