package org.archivos;

import org.modelo.unidades.Unidad;
import org.modelo.unidades.Lord;
import org.modelo.unidades.Bando;
import org.modelo.equipamiento.Equipamiento;

import java.util.*;

public final class EjercitoLoader {

  private EjercitoLoader() {}

  public static List<Unidad> cargar(String resourcePath, Map<String, Equipamiento> arsenalPorNombre) {
    var rows = CsvReader.readResource(resourcePath);
    // Ignorar header si existe (ej: "bando,nombre,tipo...")
    if (!rows.isEmpty() && rows.get(0).get(0).equalsIgnoreCase("bando"))
      rows = rows.subList(1, rows.size());

    List<Unidad> out = new ArrayList<>();
    for (var r : rows) {
      // Asume formato: bando,nombre,tipo,HP,ATK,DEF,MGC,MOV,equipamiento
      Bando bando   = Bando.valueOf(r.get(0).toUpperCase()); // REINO_DRUIDA / REINO_NIGROMANTICO
      String nombre = r.get(1);
      String tipo   = r.get(2).toUpperCase();                // LORD / UNIDAD
      int HP  = Integer.parseInt(r.get(3));
      int ATK = Integer.parseInt(r.get(4));
      int DEF = Integer.parseInt(r.get(5));
      int MGC = Integer.parseInt(r.get(6));
      int MOV = Integer.parseInt(r.get(7));
      String eqName = r.get(8);
      
      Equipamiento eq = arsenalPorNombre.get(eqName);
      if (eq == null) {
          System.out.println("Advertencia: Equipamiento '" + eqName + "' no encontrado en el arsenal. Asignando null.");
      }

      Unidad u;
      if (tipo.equals("LORD")) {
        // Usa el factory de Lord para crearlo con stats personalizadas
        u = Lord.crearPersonalizado(nombre, HP, ATK, DEF, MGC, MOV, bando);
      } else {
        u = new Unidad(nombre, HP, ATK, DEF, MGC, MOV, bando);
      }
      
      u.setEquipamiento(eq); // Asigna el equipamiento encontrado
      out.add(u);
    }
    return out;
  }
}