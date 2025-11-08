package org.archivos.factories;
import org.modelo.equipamiento.estrategias.EstrategiaBaculo;

public interface EstrategiaFactory {
    EstrategiaBaculo crear(String subtipo);
}