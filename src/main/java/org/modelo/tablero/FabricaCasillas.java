package org.modelo.tablero;

import java.util.HashMap;
import java.util.Map;

public final class FabricaCasillas {
    private final Map<String, java.util.function.BiFunction<Integer,Integer,Casilla>> registro = new HashMap<>();

    public FabricaCasillas registrar(String codigo, java.util.function.BiFunction<Integer,Integer,Casilla> creador) {
        if (codigo == null || creador == null) throw new IllegalArgumentException("Código/creador nulos");
        registro.put(codigo.trim().toUpperCase(), creador);
        return this;
    }

    public Casilla crearCasilla(String codigo, int fila, int col) {
        var key = codigo == null ? "" : codigo.trim().toUpperCase();
        var cmd = registro.get(key);
        if (cmd == null) throw new IllegalArgumentException("Terreno no registrado: " + codigo);
        return cmd.apply(fila, col);
    }
}