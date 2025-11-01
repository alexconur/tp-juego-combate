package org.archivos;

import org.modelo.unidades.Unidad;
import org.modelo.unidades.Lord;
import org.modelo.unidades.Bando;
import org.modelo.equipamiento.Equipamiento;

import java.util.*;

// *A* no está chequeado todavía
public final class EjercitoLoader {

  // public static List<Unidad> cargar(String resourcePath, Map<String, Equipamiento> porNombre) {
  //   var rows = CsvReader.readResource(resourcePath);
  //   if (!rows.isEmpty() && rows.get(0).get(0).equalsIgnoreCase("bando"))
  //     rows = rows.subList(1, rows.size());

  //   List<Unidad> out = new ArrayList<>();
  //   for (var r : rows) {
  //     Bando bando   = Bando.valueOf(r.get(0).toUpperCase()); // DRUIDA / NIGROMANTE
  //     String nombre = r.get(1);
  //     String tipo   = r.get(2).toUpperCase();                // LORD / UNIDAD
  //     int HP  = Integer.parseInt(r.get(3));
  //     int ATK = Integer.parseInt(r.get(4));
  //     int DEF = Integer.parseInt(r.get(5));
  //     int MGC = Integer.parseInt(r.get(6));
  //     int MOV = Integer.parseInt(r.get(7));
  //     String eqName = r.get(8);
  //     Equipamiento eq = porNombre.get(eqName);
  //     if (eq == null) throw new IllegalArgumentException("Equipamiento inexistente: " + eqName);

  //     Unidad u;
  //     if (tipo.equals("LORD")) {
  //       // TODO: ajustar al constructor real de tu Lord
  //       u = new Lord(nombre, HP, ATK, DEF, MGC, MOV, bando); // *A* hay que hacer get, por eso falla
  //     } else {
  //       // TODO: ajustar al constructor real de tu Unidad
  //       u = new Unidad(nombre, HP, ATK, DEF, MGC, MOV, bando);
  //     }
  //     out.add(u);
  //   }
  //   return out;
  // }
}
