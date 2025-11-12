package org.modelo.unidades;
import java.util.Random;

public enum Bando {
    REINO_DRUIDA,
    REINO_NIGROMANTICO;

    public static Bando random(Random rnd) {
        Bando[] v = values();
        return v[rnd.nextInt(v.length)];
    }
}