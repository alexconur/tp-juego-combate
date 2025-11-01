package org.archivos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;

import org.modelo.equipamiento.Arma;
import org.modelo.equipamiento.Baculo;
import org.modelo.equipamiento.Equipamiento;
import org.modelo.equipamiento.Grimorio;
import org.modelo.equipamiento.TipoArma;
import org.modelo.equipamiento.TipoBaculo;

public final class EquipamientoLoader {

  public static Map<String, Equipamiento> cargarPorNombre(String resourcePath) {
    var rows = CsvReader.readResource(resourcePath);
    if (!rows.isEmpty() && rows.get(0).get(0).equalsIgnoreCase("nombre"))
      rows = rows.subList(1, rows.size());

    List<Equipamiento> lista = new ArrayList<>();
    for (var r : rows) {
      String nombre          = r.get(0);
      String tipoEquip       = r.get(1).toUpperCase(); // ARMA/GRIMORIO/BACULO
      String subtipo         = r.get(2).isBlank() ? null : r.get(2).toUpperCase();
      int    potencia        = Integer.parseInt(r.get(3));
      int    alcance         = Integer.parseInt(r.get(4));
      int    usos            = Integer.parseInt(r.get(5));

      Equipamiento eq;
      switch (tipoEquip) {
        case "ARMA" -> {
          TipoArma t = TipoArma.valueOf(subtipo); // ESPADA/HACHA/ARCO/...
          eq = new Arma(nombre, t, potencia, alcance, usos);
        }
        case "GRIMORIO" -> {
          eq = new Grimorio(nombre, potencia, alcance, usos);
        }
        case "BACULO" -> {
          TipoBaculo tb = TipoBaculo.valueOf(subtipo); // CURACION/SANACION/FORTALEZA
          eq = new Baculo(nombre, tb, potencia, usos, alcance);
        }
        default -> throw new IllegalArgumentException("tipo_equipamiento desconocido: " + tipoEquip);
      }
      lista.add(eq);
    }
    return lista.stream().collect(toMap(Equipamiento::getNombre, e -> e));
  }
}
