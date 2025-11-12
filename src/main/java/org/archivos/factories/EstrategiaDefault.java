package org.archivos.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.modelo.equipamiento.estrategias.EstrategiaBaculo;
import org.modelo.equipamiento.estrategias.EstrategiaCuracion;
import org.modelo.equipamiento.estrategias.EstrategiaFortaleza;
import org.modelo.equipamiento.estrategias.EstrategiaSanacion;

public class EstrategiaDefault implements EstrategiaFactory {
    private final Map<String, Supplier<EstrategiaBaculo>> registro = new HashMap<>();

    public EstrategiaDefault() {
        registrar("CURACION", EstrategiaCuracion::new);
        registrar("SANACION", EstrategiaSanacion::new);
        registrar("FORTALEZA", EstrategiaFortaleza::new);
    }

    private void registrar(String tipo, Supplier<EstrategiaBaculo> constructor) {
        registro.put(tipo.toUpperCase(), constructor);
    }

    @Override
    public EstrategiaBaculo crear(String subtipo) {
        Supplier<EstrategiaBaculo> constructor = registro.get(subtipo.toUpperCase());
        if (constructor == null) {
            throw new IllegalArgumentException("Tipo de Báculo desconocido: " + subtipo);
        }
        return constructor.get();
    }
}