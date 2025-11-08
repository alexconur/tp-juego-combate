package org.archivos.factories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.modelo.unidades.Bando;
import org.modelo.unidades.Lord;
import org.modelo.unidades.Tropa;
import org.modelo.unidades.Unidad;

public class UnidadDefault implements UnidadFactory {

    private final Map<String, Function<List<String>, Unidad>> registro = new HashMap<>();

    private static final int COL_BANDO = 0;
    private static final int COL_NOMBRE = 1;
    private static final int COL_TIPO = 2;
    private static final int COL_HP = 3;
    private static final int COL_ATK = 4;
    private static final int COL_DEF = 5;
    private static final int COL_MGC = 6;
    private static final int COL_MOV = 7;

    public UnidadDefault() {
        registrar("LORD", this::crearLord);
        registrar("TROPA", this::crearTropa);
        registrar("UNIDAD", this::crearTropa);
    }
    
    private void registrar(String tipo, Function<List<String>, Unidad> creador) {
        registro.put(tipo.toUpperCase(), creador);
    }

    @Override
    public Unidad crear(List<String> row) {
        String tipo = row.get(COL_TIPO).toUpperCase();
        Function<List<String>, Unidad> creador = registro.get(tipo);
        if (creador == null) {
            throw new IllegalArgumentException("Tipo de unidad desconocido: " + tipo);
        }
        return creador.apply(row);
    }
    
    private Lord crearLord(List<String> row) {
        return Lord.crearPersonalizado(
            row.get(COL_NOMBRE),
            Integer.parseInt(row.get(COL_HP)),
            Integer.parseInt(row.get(COL_ATK)),
            Integer.parseInt(row.get(COL_DEF)),
            Integer.parseInt(row.get(COL_MGC)),
            Integer.parseInt(row.get(COL_MOV)),
            Bando.valueOf(row.get(COL_BANDO).toUpperCase())
        );
    }

    private Tropa crearTropa(List<String> row) {
        return new Tropa(
            row.get(COL_NOMBRE),
            Integer.parseInt(row.get(COL_HP)),
            Integer.parseInt(row.get(COL_ATK)),
            Integer.parseInt(row.get(COL_DEF)),
            Integer.parseInt(row.get(COL_MGC)),
            Integer.parseInt(row.get(COL_MOV)),
            Bando.valueOf(row.get(COL_BANDO).toUpperCase())
        );
    }
}