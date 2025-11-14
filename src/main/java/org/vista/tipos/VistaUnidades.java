package org.vista.tipos;

import java.util.List;
import java.util.Scanner;

import org.controlador.Colores;

public class VistaUnidades {

    private final Scanner sc = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        List<String> lineas = List.of(
                "[0] Volver al menú de acciones",
                "[1] Ver unidades desplegadas",
                "[2] Ver unidades en reserva",
                "[3] Ver detalle de una unidad"
        );

        imprimirCajaLista("Menú de Unidades", lineas);
        return leerEnteroEnRango("Opción", 0, 3);
    }

    public void mostrarListaUnidades(String titulo, List<String> nombres, List<String> equipos, List<String> colores) {

        List<String> lineas = new java.util.ArrayList<>();

        for (int i = 0; i < nombres.size(); i++) {
            String nombre = nombres.get(i);
            String equip = equipos.get(i);
            String color = colores.get(i);

            String texto = String.format("%s%s (%s)%s", color, nombre, equip, Colores.RESET);
            lineas.add(texto);
        }

        if (lineas.isEmpty()) lineas.add("(vacío)");
        imprimirCajaLista(titulo, lineas);
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

        List<String> lineas = new java.util.ArrayList<>();
        lineas.add("[0] Cancelar");

        for (int i = 0; i < nombres.size(); i++) {
            String linea = String.format(
                    "[%d] %s %-8s  %s/%s HP [%s]",
                    i + 1, nombres.get(i), posiciones.get(i),
                    hps.get(i), maxHps.get(i), estados.get(i)
            );

            lineas.add(colores.get(i) + linea + Colores.RESET);
        }

        imprimirCajaLista("Seleccionar Unidad", lineas);
        return leerEnteroEnRango("Opción", 0, nombres.size());
    }

    public int seleccionarUnidadSimple(List<String> lineasDeInfo, List<String> colores) {

        List<String> lineas = new java.util.ArrayList<>();
        lineas.add("[0] Cancelar");

        for (int i = 0; i < lineasDeInfo.size(); i++) {
            String linea = "[" + (i + 1) + "] " + lineasDeInfo.get(i);
            lineas.add(colores.get(i) + linea + Colores.RESET);
        }

        imprimirCajaLista("Seleccionar Unidad", lineas);

        return leerEnteroEnRango("Opción", 0, lineasDeInfo.size());
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

        List<String> lineas = new java.util.ArrayList<>();

        lineas.add(String.format("Nombre:     %s", nombre));
        lineas.add(String.format("Bando:      %s", bando));
        lineas.add(String.format("Posición:   %s", pos));
        lineas.add(String.format("HP:         %s / %s", hp, maxHp));
        lineas.add("────────────────────────────────────────────");
        lineas.add(String.format("ATK total:  %s", atk));
        lineas.add(String.format("DEF total:  %s", def));
        lineas.add(String.format("MGC total:  %s", mgc));
        lineas.add(String.format("Movimiento: %s/%s", movRestante, movTotal));
        lineas.add("────────────────────────────────────────────");

        if (eqNombre != null && !eqNombre.isEmpty()) {
            lineas.add(String.format("Arma:     %s", eqNombre));
            lineas.add(String.format("Ofensivo: %s", eqOfensivo));
            lineas.add(String.format("Rango:    %s", eqRango));
            lineas.add(String.format("Usos:     %s", eqUsos));
        } else {
            lineas.add("Arma:       (ninguna)");
        }

        lineas.add("────────────────────────────────────────────");
        lineas.add(String.format("Estado:     %s", estadoCompleto));

        imprimirCajaLista("Detalle de Unidad", lineas);
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

    private void imprimirCajaLista(String titulo, List<String> lineas) {
        titulo = " " + titulo.toUpperCase() + " ";

        int ancho = titulo.length();
        for (String l : lineas)
            ancho = Math.max(ancho, limpiarANSI(l).length());

        String arriba    = "╔" + "═".repeat(ancho) + "╗";
        String medio = "║" + centrar(titulo, ancho) + "║";
        String sep    = "╠" + "─".repeat(ancho) + "╣";
        String abajo = "╚" + "═".repeat(ancho) + "╝";

        System.out.println();
        System.out.println(arriba);
        System.out.println(medio);
        System.out.println(sep);

        for (String l : lineas) {
            String clean = limpiarANSI(l);
            System.out.println("║" + l + " ".repeat(ancho - clean.length()) + "║");
        }

        System.out.println(abajo);
    }

    private String centrar(String s, int ancho) {
        int esp = ancho - s.length();
        return " ".repeat(esp / 2) + s + " ".repeat(esp - esp / 2);
    }

    // Sirve para evitar: bordes mal alineados, texto que se escapa de la caja, cajas más chicas que el contenido, etc.
    private String limpiarANSI(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*[A-Za-z]", "");
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
