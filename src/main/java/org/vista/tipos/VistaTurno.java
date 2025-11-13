package org.vista.tipos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.controlador.Colores;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.InfoCasilla;

public class VistaTurno {

    private final Scanner sc = new Scanner(System.in);

    public void mostrarEstado(
            boolean isGameOver, 
            int numeroTurno, 
            String bandoNombre, 
            String bandoColor,
            String tableroRenderizado,
            List<String> lineasDeUnidad,
            List<String> coloresUnidad
        ) {

        if (isGameOver) {
            return;
        }

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("TURNO NÚMERO: " + numeroTurno);
        System.out.println("TURNO DE: " + bandoColor + bandoNombre + Colores.RESET);
        System.out.println("══════════════════════════════════════════════");
        
        System.out.println(tableroRenderizado);

        List<String> lineas = new java.util.ArrayList<>();
        for (int i = 0; i < lineasDeUnidad.size(); i++) {
            lineas.add(coloresUnidad.get(i) + lineasDeUnidad.get(i) + Colores.RESET);
        }

        imprimirCajaLista("Unidades en Tablero", lineas);
    }

    public int mostrarMenuPrincipal() {
        List<String> acciones = new java.util.ArrayList<>();
        acciones.add("[1] Mover            [2] Atacar/Curar     [3] Ver unidades");
        acciones.add("[4] Desplegar        [5] Emboscada        [6] Información casillas");
        acciones.add("[7] Terminar turno   [8] Rendirse");

        imprimirCajaLista("Acciones", acciones);
        return leerEnteroEnRango("Opción", 1, 8);
    }

    public int seleccionarUnidad(String prompt, List<String> lineasFormateadas, String colorLista) {
        if (lineasFormateadas.isEmpty()) {
            System.out.println("\nNo hay unidades disponibles para " + prompt + ".");
            return 0;
        }

        List<String> lista = new java.util.ArrayList<>();
        lista.add("[0] Cancelar");

        for (String linea : lineasFormateadas)
            lista.add(colorLista + linea + Colores.RESET);

        imprimirCajaLista("Seleccionar Unidad (" + prompt.toUpperCase() + ")", lista);

        int idx = leerEnteroEnRango("Opción", 0, lineasFormateadas.size());
        return idx;
    }

    public int seleccionarUnidadReserva(List<String> lineasFormateadas, String colorLista) {
        if (lineasFormateadas.isEmpty()) {
            System.out.println("No hay unidades en la reserva.");
            return 0;
        }

        List<String> lista = new java.util.ArrayList<>();
        lista.add("[0] Cancelar");

        for (String linea : lineasFormateadas)
            lista.add(colorLista + linea + Colores.RESET);

        imprimirCajaLista("Seleccionar de Reserva", lista);

        int idx = leerEnteroEnRango("Opción", 0, lineasFormateadas.size());
        return idx;
    }

    public void mostrarCasillasDisponibles(String tableroRenderizado) {

        String titulo = " CASILLAS ALCANZABLES ";
        int ancho = titulo.length();

        String arriba    = "╔" + "═".repeat(ancho) + "╗";
        String medio = "║" + titulo + "║";
        String abajo = "╚" + "═".repeat(ancho) + "╝";

        System.out.println();
        System.out.println(arriba);
        System.out.println(medio);
        System.out.println(abajo);

        System.out.println(tableroRenderizado);
    }

    public UbicacionInicio pedirUbicacion(String mensaje) {
        System.out.println("-- " + mensaje + " --");
        int fila = leerEntero("Fila");
        int col = leerEntero("Columna");
        return new UbicacionInicio(fila, col);
    }

    public void mostrarEmboscadaExitosa(String nombreUnidad) {
        System.out.println("🕵️ " + nombreUnidad + " se ha ocultado en el bosque. ¡El enemigo no podrá verla!");
    }

    public void mostrarEmboscadaInvalida(String nombreUnidad) {
        System.out.println("❌ " + nombreUnidad + " no puede preparar emboscada aquí.");
    }

    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String linea = sc.nextLine();
                return Integer.parseInt(linea.trim());
            } catch (InputMismatchException | NumberFormatException e) {
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

    public void mostrarInfoCasillas(){
        final String RESET = Colores.RESET;
        StringBuilder sb = new StringBuilder();

        sb.append("\n╔═══════════════════════ INFORMACIÓN DE CASILLAS ═══════════════════════╗\n");

        for (InfoCasilla info : Casilla.obtenerInformacion()) {
            String bloque = info.getColor() + "   " + RESET;
            sb.append(String.format("   %s  %-18s: %s%n", bloque, info.getNombre(), info.getDescripcion()));
        }

        sb.append("╚═══════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(sb.toString());
        System.out.println("Presione Enter para continuar...");
        sc.nextLine();
    }

    private void imprimirCajaLista(String titulo, List<String> lineas) {

        String tituloTxt = " " + titulo.toUpperCase() + " ";

        int ancho = tituloTxt.length();
        for (String l : lineas) {
            String limpio = l.replaceAll("\u001B\\[[;\\d]*m", "");
            ancho = Math.max(ancho, limpio.length());
        }

        String arriba    = "╔" + "═".repeat(ancho) + "╗";
        String titul  = "║" + centrar(tituloTxt, ancho) + "║";
        String sep    = "║" + "─".repeat(ancho) + "║";
        String abajo = "╚" + "═".repeat(ancho) + "╝";

        System.out.println();
        System.out.println(arriba);
        System.out.println(titul);
        System.out.println(sep);

        for (String l : lineas) {
            String limpio = l.replaceAll("\u001B\\[[;\\d]*m", "");
            System.out.println("║" + l + " ".repeat(ancho - limpio.length()) + "║");
        }

        System.out.println(abajo);
    }


    private String centrar(String s, int ancho) {
        int espacios = ancho - s.length();
        int izq = espacios / 2;
        int der = espacios - izq;
        return " ".repeat(izq) + s + " ".repeat(der);
    }
}
