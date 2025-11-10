package org.vista.tipos;

import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.modelo.equipamiento.Equipamiento;

import java.util.List;
import java.util.Scanner;

public class VistaUnidades {

    private final Scanner sc = new Scanner(System.in);

    // Menú principal
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

    // Mostrar lista de unidades (en tablero o reserva)
    public void mostrarListaUnidades(String titulo, List<Unidad> unidades) {

        String tituloCaja = String.format(" %S ", titulo); // %S lo pone en mayúsculas
        // Cuadro UNIDADES EN RESERVA
        System.out.printf("\n╔═════════════════%s════════════════╗\n", tituloCaja);
        imprimirLineasDeUnidades(unidades, false); // false = no mostrar índices
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }      

    // Seleccionar unidad de una lista
    public Unidad seleccionarUnidad(List<Unidad> unidades) {
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades disponibles.");
            return null;
        }
        System.out.println("\n╔══════════════════ SELECCIONAR UNIDAD ════════════════╗");
        System.out.println("║ [0] Cancelar                                         ║");
        imprimirLineasDeUnidades(unidades, true); // true = mostrar índices [1], [2], ...
        System.out.println("╚══════════════════════════════════════════════════════╝");

        int opt = leerEnteroEnRango("Opción (0 para cancelar)", 0, unidades.size());
        return (opt == 0) ? null : unidades.get(opt - 1);
    }

    // Refactorizamos la lógica de imprimir la lista de unidades para reusarla en mostrarListaUnidades y seleccionarUnidad.
    private void imprimirLineasDeUnidades(List<Unidad> unidades, boolean conIndices) {
        if (unidades.isEmpty()) {
            System.out.println("║ (vacío)                                              ║");
            return;
        }

        int i = 1;
        for (Unidad u : unidades) {
            String pos = (u.getCasillaActual() != null)
                    ? "(" + u.getCasillaActual().getFila() + "," + u.getCasillaActual().getColumna() + ")"
                    : "(reserva)";
            String estado = (u.estaVivo() ? "VIVO" : "MUERTO");
            
            String color = (u.estaVivo()) ? Colores.colorParaBando(u.getBando()) : Colores.VACIO_U;
            String prefijo = (conIndices) ? String.format("[%d] ", i++) : "";
            
            // Formateamos la línea
            String linea = String.format("%s%-18s %-10s %s/%s HP [%s]",
                    prefijo, u.getNombre(), pos, u.getHp(), u.getMaxHp(), estado);
            
            // Imprimimos dentro de la caja con padding
            System.out.printf("║ %s%-52s%s ║%n", color, linea, Colores.RESET);
        }
    }

    // Muestra las unidades vivas de una lista y permite seleccionar una
    public Unidad seleccionarUnidadViva(List<Unidad> unidades) {
        List<Unidad> vivas = unidades.stream()
                .filter(Unidad::estaVivo)
                .toList();

        if (vivas.isEmpty()) {
            System.out.println("No hay unidades vivas en el tablero.");
            return null;
        }

        return seleccionarUnidad(vivas);
    }

    // Mostrar detalle completo de una unidad
    public void mostrarDetalleUnidad(Unidad u) {
        if (u == null) {
            System.out.println("Unidad inválida.");
            return;
        }
        
        String estado = u.estaVivo() ? "Vivo" : "Muerto";
        estado += u.isOculto() ? " (Oculto)" : "";
        estado += u.isLord() ? " [LORD]" : "";

        String pos = (u.getCasillaActual() != null 
                ? "(" + u.getCasillaActual().getFila() + "," + u.getCasillaActual().getColumna() + ")" 
                : "Reserva");

        // Ancho de la caja: 42 caracteres
        System.out.println("\n╔═══════════ DETALLE DE UNIDAD ═══════════╗");
        System.out.printf("║ %-12s %-26s ║%n", "Nombre:", u.getNombre());
        System.out.printf("║ %-12s %-26s ║%n", "Bando:", u.getBando());
        System.out.printf("║ %-12s %-26s ║%n", "Posición:", pos);
        System.out.printf("║ %-12s %-26s ║%n", "HP:", u.getHp() + " / " + u.getMaxHp(), Colores.RESET);
        System.out.println("║─────────────────────────────────────────║");
        System.out.printf("║ %-12s %-26s ║%n", "ATK total:", u.getAtkTotal(), Colores.RESET);
        System.out.printf("║ %-12s %-26s ║%n", "DEF total:", u.getDef(), Colores.RESET);
        System.out.printf("║ %-12s %-26s ║%n", "MGC total:", u.getMgcTotal(), Colores.RESET);
        System.out.printf("║ %-12s %-26s ║%n", "Movimiento:", u.getMovimientoRestante() + "/" + u.getMov());
        System.out.println("║─────────────────────────────────────────║");
        
        // Mostrar equipamiento
        Equipamiento eq = u.getEquipamiento();
        if (eq != null) {
            String rangoDisplay;
            if (eq.esOfensivo()) {
                rangoDisplay = String.valueOf(eq.getRango());
            } else {
                rangoDisplay = "Todos los aliados";
            }
            System.out.printf("║ %-12s %-26s ║%n", "Arma:", eq.getNombre());            
            System.out.printf("║   %-10s %-26s ║%n", "Ofensivo: ", (eq.esOfensivo() ? "Sí" : "No"));
            System.out.printf("║   %-10s %-26s ║%n", "Rango: ", rangoDisplay);
            System.out.printf("║   %-10s %-26s ║%n", "Usos restantes: ", + eq.getUsosRestantes());
        } else {
            System.out.printf("║ %-12s (ninguno)%-15s ║%n", "Arma:", "");
        }
        
        System.out.println("║─────────────────────────────────────────║");
        System.out.printf("║ %-12s %-26s ║%n", "Estado:", estado);
        System.out.println("╚═════════════════════════════════════════╝\n");
    }
    // Método de lectura robusto
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
