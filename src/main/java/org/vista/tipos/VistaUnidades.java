package org.vista.tipos;

import java.util.List;
import java.util.Scanner;

import org.vista.Colores;

public class VistaUnidades {

    private final Scanner sc = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        System.out.println("\n╔══════════ MENÚ DE UNIDADES ══════════╗");
        System.out.println("║ [1] Ver unidades desplegadas         ║");
        System.out.println("║ [2] Ver unidades en reserva          ║");
        System.out.println("║ [3] Ver detalle de una unidad        ║");
        System.out.println("║                                      ║");
        System.out.println("║ [0] Volver al menú de acciones       ║");
        System.out.println("╚══════════════════════════════════════╝"); 
        return leerEnteroEnRango("Opción", 0, 3);
    }

    public void mostrarListaUnidades(String titulo, List<String> nombres, List<String> equipos) {

        String tituloCaja = String.format(" %S ", titulo);
        System.out.printf("\n╔═════════════════%s════════════════╗\n", tituloCaja);
        imprimirLineasDeUnidadesSimple(nombres, equipos, false);
        
        System.out.println("╚══════════════════════════════════════════════════════╝");
    } 

    public int seleccionarUnidad(
            List<String> nombres, 
            List<String> posiciones, 
            List<String> hps, 
            List<String> maxHps, 
            List<String> estados, 
            List<String> colores) {

        if (nombres.isEmpty()) {
            System.out.println("No hay unidades disponibles.");
            return 0;
        }
        
        System.out.println("\n╔══════════════════ SELECCIONAR UNIDAD ════════════════╗");
        System.out.println("║ [0] Cancelar                                         ║");

        imprimirLineasDeUnidadesDetallado(nombres, posiciones, hps, maxHps, estados, colores, true);
        
        System.out.println("╚══════════════════════════════════════════════════════╝");
        int opt = leerEnteroEnRango("Opción (0 para cancelar)", 0, nombres.size());
        return opt;
    }

    private void imprimirLineasDeUnidadesSimple(List<String> nombres, List<String> equips, boolean conIndices) {
        if (nombres.isEmpty()) {
            System.out.println("║ (vacío)                                              ║");
            return;
        }

        int i = 1;
        // Iteramos sobre las listas usando un índice
        for (int j = 0; j < nombres.size(); j++) {
            
            String nombre = nombres.get(j);
            String equip = equips.get(j);

            // Formato de la VISTA (simple)
            String linea = String.format("%-15s (%s)", nombre, equip);
            
            if (conIndices) {
                linea = String.format("[%d] %s", i++, linea);
            } else {
                linea = "  · " + linea; // Formato para lista simple
            }
            
            // Ajusta el padding para que coincida con el ancho de tu caja (54 caracteres)
            System.out.printf("║ %-50s ║%n", linea);
        }
    }

    private void imprimirLineasDeUnidadesDetallado(
            List<String> nombres, 
            List<String> posiciones, 
            List<String> hps, 
            List<String> maxHps, 
            List<String> estados, 
            List<String> colores, // Colores pre-calculados por el controlador
            boolean conIndices) {

        if (nombres.isEmpty()) { // Comprobamos con la lista de nombres
            System.out.println("║ (vacío)                                              ║");
            return;
        }

        int i = 1;
        // Usamos un loop por índice para acceder a todas las listas paralelas
        for (int j = 0; j < nombres.size(); j++) {
            
            // Obtenemos los datos de las listas (todos son Strings)
            String nombre = nombres.get(j);
            String pos = posiciones.get(j);
            String hp = hps.get(j);
            String maxHp = maxHps.get(j);
            String estado = estados.get(j);
            String color = colores.get(j); // El controlador ya resolvió el color

            // --- El resto de tu lógica de formateo es idéntica ---
            
            String prefijo = (conIndices) ? String.format("[%d] ", i++) : "";
            
            // Formateamos la línea
            String linea = String.format("%s%-18s %-10s %s/%s HP [%s]",
                    prefijo, nombre, pos, hp, maxHp, estado);
            
            // Imprimimos dentro de la caja con padding
            // (Asumiendo que 'Colores.RESET' es una constante estática en tu clase Vista)
            System.out.printf("║ %s%-52s%s ║%n", color, linea, Colores.RESET);
        }
    }

    public int seleccionarUnidadSimple(List<String> lineasDeInfo, List<String> colores) {
        
        System.out.println("\n╔══════════════════ SELECCIONAR UNIDAD ════════════════╗");
        System.out.println("║ [0] Cancelar                                         ║");

        // --- Bucle de impresión simple ---
        // La vista solo se encarga de agregar el índice "[1]", "[2]", etc.
        int i = 1;
        for (int j = 0; j < lineasDeInfo.size(); j++) {
            
            String color = colores.get(j);
            String prefijo = String.format("[%d] ", i++);
            
            // Concatena el índice "[1] " con la línea de info "Nombre (0,0)..."
            String lineaCompleta = prefijo + lineasDeInfo.get(j); 

            // Imprime la línea (ajusta el -52 si tu prefijo cambia el ancho)
            System.out.printf("║ %s%-52s%s ║%n", color, lineaCompleta, Colores.RESET);
        }
        // --- Fin del bucle ---
        
        System.out.println("╚══════════════════════════════════════════════════════╝");
        
        int opt = leerEnteroEnRango("Opción (0 para cancelar)", 0, lineasDeInfo.size());
        return opt;
    }

    public void mostrarDetalleUnidad(
        String nombre, String bando, String pos, String hp, String maxHp, 
        String atk, String def, String mgc, String movRestante, String movTotal,
        String eqNombre, String eqOfensivo, String eqRango, String eqUsos,
        String estadoCompleto) {

        if (nombre == null || nombre.isEmpty()) {
            System.out.println("Unidad inválida.");
            return;
        }
    
        System.out.println("\n╔═══════════ DETALLE DE UNIDAD ═══════════╗");
        System.out.printf("║ %-12s %-26s ║%n", "Nombre:", nombre);
        System.out.printf("║ %-12s %-26s ║%n", "Bando:", bando);
        System.out.printf("║ %-12s %-26s ║%n", "Posición:", pos);
        System.out.printf("║ %-12s %-26s ║%n", "HP:", hp + " / " + maxHp, Colores.RESET);
        System.out.println("║─────────────────────────────────────────║");
        System.out.printf("║ %-12s %-26s ║%n", "ATK total:", atk, Colores.RESET);
        System.out.printf("║ %-12s %-26s ║%n", "DEF total:", def, Colores.RESET);
        System.out.printf("║ %-12s %-26s ║%n", "MGC total:", mgc, Colores.RESET);
        System.out.printf("║ %-12s %-26s ║%n", "Movimiento:", movRestante + "/" + movTotal);
        System.out.println("║─────────────────────────────────────────║");
        
            
        if (eqNombre != null && !eqNombre.isEmpty()) {
            System.out.printf("║ %-12s %-26s ║%n", "Arma:", eqNombre);            
            System.out.printf("║   %-10s %-26s ║%n", "Ofensivo: ", eqOfensivo);
            System.out.printf("║   %-10s %-26s ║%n", "Rango: ", eqRango);
            System.out.printf("║   %-10s %-26s ║%n", "Usos restantes: ", eqUsos);
        } else {
            System.out.printf("║ %-12s (ninguno)%-15s ║%n", "Arma:", "");
        }
        
        System.out.println("║─────────────────────────────────────────║");
        System.out.printf("║ %-12s %-26s ║%n", "Estado:", estadoCompleto);
        System.out.println("╚═════════════════════════════════════════╝\n");
    }

    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String linea = sc.nextLine();
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
            }
        }
    }

    // Usando el nuevo leerEntero
    private int leerEnteroEnRango(String prompt, int min, int max) {
         int valor;
        while (true) {
            valor = leerEntero(prompt);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("Opción fuera de rango (" + min + "-" + max + "). Intente de nuevo.");
        }
    }

    public void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
