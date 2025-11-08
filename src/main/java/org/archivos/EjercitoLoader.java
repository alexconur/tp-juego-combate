package org.archivos;

import java.util.ArrayList;
import java.util.List;

import org.modelo.equipamiento.*;
import org.modelo.equipamiento.estrategias.*;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Lord;
import org.modelo.unidades.Unidad;
import org.modelo.unidades.Tropa;


public final class EjercitoLoader {

  private EjercitoLoader() {}

  public static List<Unidad> cargar(String resourcePath) {
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
        
      String eqData = r.get(8);
      Equipamiento eq = null;
      if (eqData.contains("|")) {
        String[] partes = eqData.split("\\|");
        String nombreEq = partes[0];
        String tipoEq   = partes[1];
        String subtipo  = partes[2].isBlank() ? null : partes[2];
        int potencia    = Integer.parseInt(partes[3]);
        int alcance     = Integer.parseInt(partes[4]);
        int usos        = Integer.parseInt(partes[5]);

        switch (tipoEq.toUpperCase()) {
            case "ARMA" -> eq = new Arma(nombreEq, TipoArma.valueOf(subtipo.toUpperCase()), potencia, alcance, usos);
            case "GRIMORIO" -> eq = new Grimorio(nombreEq, potencia, alcance, usos);
            case "BACULO" -> {
              EstrategiaBaculo estrategia;
              String subtipoUpper = subtipo.toUpperCase();
                
              switch (subtipoUpper) {
                case "CURACION" -> estrategia = new EstrategiaCuracion();
                case "SANACION" -> estrategia = new EstrategiaSanacion();
                case "FORTALEZA" -> estrategia = new EstrategiaFortaleza();
                default -> throw new IllegalArgumentException("Tipo de Báculo desconocido: " + subtipo);
              }                
              int poder = potencia;
              int bonusMagia = potencia; 
              eq = new Baculo(nombreEq, estrategia, bonusMagia, usos, poder);
            }              
            default -> throw new IllegalArgumentException("Tipo de equipamiento desconocido: " + tipoEq);
        }
      }

      Unidad u;
      if (tipo.equals("LORD")) {
        // Usa el factory de Lord para crearlo con stats personalizadas
        u = Lord.crearPersonalizado(nombre, HP, ATK, DEF, MGC, MOV, bando);
      } else {
        u = new Tropa(nombre, HP, ATK, DEF, MGC, MOV, bando);
      }
      
      u.setEquipamiento(eq); // Asigna el equipamiento encontrado
      out.add(u);
    }
    return out;
  }
}