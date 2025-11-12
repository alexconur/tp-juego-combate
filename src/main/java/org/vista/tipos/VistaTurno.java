package org.vista.tipos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.modelo.tablero.Casilla;
import org.modelo.tablero.InfoCasilla;
import org.vista.Colores;

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
        
        System.out.println("\n╔══════════════════════════ UNIDADES EN TABLERO ══════════════════════════╗");

        for (int i = 0; i < lineasDeUnidad.size(); i++) {
            String linea = lineasDeUnidad.get(i);
            String color = coloresUnidad.get(i);
            System.out.println("║ " + color + linea + " " + Colores.RESET + " ║");
        }
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n╔═════════════════════════ ACCIONES ═════════════════════════════════╗");
        System.out.println("║ [1] Mover           [2] Atacar/Curar      [3] Ver unidades         ║");
        System.out.println("║ [4] Desplegar       [5] Emboscada         [6] Informacion casillas ║");
        System.out.println("║ [7] Terminar turno  [8] Rendirse                                   ║");        
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");   
        return leerEnteroEnRango("Opción", 1, 8);
    }
    
    public int seleccionarUnidad(String prompt, List<String> lineasFormateadas, String colorLista) {
        if (lineasFormateadas.isEmpty()) {
            System.out.println("\nNo hay unidades disponibles para " + prompt + ".");
            return 0;
        }
        
        System.out.println("\n╔═══ SELECCIONAR UNIDAD (" + prompt.toUpperCase() + ") ═══╗");
        System.out.println("║ [0] Cancelar                   ║");

        for (int i = 0; i < lineasFormateadas.size(); i++) {
            
            String linea = lineasFormateadas.get(i);
            System.out.printf("║ %s%-30s%s ║%n", colorLista, linea, Colores.RESET);
        }
        System.out.println("╚══════════════════════════════════╝");

        int idx = leerEnteroEnRango("Opción", 0, lineasFormateadas.size());
        return idx;
    }
    
    public int seleccionarUnidadReserva(List<String> lineasFormateadas, String colorLista) {
        if (lineasFormateadas.isEmpty()) {
            System.out.println("No hay unidades en la reserva.");
            return 0;
        }
        
        System.out.println("\n╔═══ SELECCIONAR DE RESERVA ═══╗");
        System.out.println("║ [0] Cancelar                 ║");        
        
        for (int i = 0; i < lineasFormateadas.size(); i++) {
            String linea = lineasFormateadas.get(i);
            System.out.printf("║ %s%-30s%s ║%n", colorLista, linea, Colores.RESET);
        }
        System.out.println("╚══════════════════════════════╝");
                
        int idx = leerEnteroEnRango("Opción", 0, lineasFormateadas.size());
        return idx;
    }

    public void mostrarCasillasDisponibles(String tableroRenderizado) {
        System.out.println("\n═════════ CASILLAS ALCANZABLES ═════════");
        System.out.println(tableroRenderizado);
        System.out.println("═════════════════════════════════════════");
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
}