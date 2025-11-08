package org.archivos.factories;
import java.util.List;

import org.modelo.unidades.Unidad;

public interface UnidadFactory {
    Unidad crear(List<String> row);
}