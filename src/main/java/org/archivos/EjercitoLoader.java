package org.archivos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.archivos.factories.EquipamientoFactory;
import org.archivos.factories.UnidadFactory; // Interfaz
import org.modelo.equipamiento.Equipamiento;       // Interfaz
import org.modelo.unidades.Unidad;

public class EjercitoLoader {

    private static final int COL_EQUIPAMIENTO = 8;

    private final UnidadFactory unidadFactory;
    private final EquipamientoFactory equipamientoFactory;

    public EjercitoLoader(UnidadFactory unidadFactory, EquipamientoFactory equipamientoFactory) {
        this.unidadFactory = unidadFactory;
        this.equipamientoFactory = equipamientoFactory;
    }
    
    private boolean filaEsHeader(List<String> row) {
        return !row.isEmpty() && row.get(0).equalsIgnoreCase("bando");
    }

    public List<Unidad> cargar(String resourcePath) {
        var rows = CsvReader.readResource(resourcePath);

        var dataRows = rows.stream()
            .skip(filaEsHeader(rows.get(0)) ? 1 : 0)
            .collect(Collectors.toList());

        List<Unidad> ejercito = new ArrayList<>();
        for (var row : dataRows) {
            Unidad unidad = this.unidadFactory.crear(row);

            String eqData = row.get(COL_EQUIPAMIENTO);
            Equipamiento eq = this.equipamientoFactory.crear(eqData);

            unidad.setEquipamiento(eq);
            ejercito.add(unidad);
        }
        return ejercito;
    }

}