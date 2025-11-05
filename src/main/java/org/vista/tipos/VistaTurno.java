package org.vista.tipos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Unidad;
import org.vista.TableroRenderer;
import org.vista.Colores;

public class VistaTurno {
    
    private final Scanner sc = new Scanner(System.in);

    public void mostrarEstado(Juego juego) {
        if (juego.isGameOver()) {
            return;
        }
        System.out.println("\n══════════════════════════════════════════════");

        String bandoColor = (juego.getBandoActual() == org.modelo.unidades.Bando.REINO_DRUIDA) ? Colores.ALIADO : Colores.ENEMIGO;


        System.out.println("TURNO DE: " + bandoColor + juego.getBandoActual() + Colores.RESET);
        System.out.println("══════════════════════════════════════════════");
        
        Tablero tablero = juego.getTablero();
        System.out.println(TableroRenderer.render(tablero, juego.getBandoActual()));
        
        System.out.println("\n╔══════════════════════════ UNIDADES EN TABLERO ══════════════════════════╗");

        for (Unidad u : juego.getTodasUnidadesEnTablero()) {
            String bando = u.getBando().toString().substring(6, 12);
            String pos = (u.getCasillaActual() != null) ? 
                         "(" + u.getCasillaActual().getFila() + "," + u.getCasillaActual().getColumna() + ")" : "(RESERVA)";
            String hp = u.getHp() + "/" + u.getMaxHp() + " HP";
            String estado = u.estaVivo() ? hp : "MUERTO";
            String acciones = "[Mov: " + (u.puedeMoverse() ? "SI" : "NO") + ", Act: " + (u.puedeActuar() ? "SI" : "NO") + "]";  

            String colorBando;
            if (u.estaVivo()){
                colorBando = (u.getBando() ==  juego.getBandoActual()) ? Colores.ALIADO : Colores.ENEMIGO;
            } else {
                colorBando = Colores.VACIO_U;
            }

            String lineaUnidad = String.format("[%s] %-18s %-10s %-12s %s", bando, u.getNombre(), pos, estado, acciones);

            // Imprimimos la línea dentro de los bordes ║ ... ║
            System.out.println("║ " + colorBando + lineaUnidad + " " + Colores.RESET + " ║");
        }
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");
    }   

    public int mostrarMenuPrincipal() {
        System.out.println("\n╔═══════════════════════ ACCIONES ═════════════════════════╗");
        System.out.println("║ [1] Mover           [2] Atacar/Curar      [3] Ver menus  ║");
        System.out.println("║ [4] Desplegar       [5] Emboscada                        ║");
        System.out.println("║ [6] Terminar turno  [7] Rendirse                         ║");        
        System.out.println("╚══════════════════════════════════════════════════════════╝");   
        
        return leerEnteroEnRango("Opción", 1, 7);
    }
    
    public Unidad seleccionarUnidad(List<Unidad> unidades, String prompt) {
        if (unidades.isEmpty()) {
            System.out.println("\nNo hay unidades disponibles para " + prompt + ".");
            return null;
        }
        
        System.out.println("\n╔═══ SELECCIONAR UNIDAD (" + prompt.toUpperCase() + ") ═══╗");
        System.out.println("║ [0] Cancelar                   ║");

        // Colorear la lista de selección
        String colorLista = Colores.ALIADO; // Default a Aliado
        if (!unidades.isEmpty() && unidades.get(0).getBando() != org.modelo.unidades.Bando.REINO_DRUIDA) { // Asunción simple
             if(prompt.equalsIgnoreCase("Atacar")) {
                colorLista = Colores.ENEMIGO;
             }
        }
        if(prompt.equalsIgnoreCase("Atacar")) colorLista = Colores.ENEMIGO;
        else if(prompt.equalsIgnoreCase("Curar") || prompt.equalsIgnoreCase("Mover")) colorLista = Colores.ALIADO;

        for (int i = 0; i < unidades.size(); i++) {
            Unidad u = unidades.get(i);
            Casilla c = u.getCasillaActual();
            String pos = (c != null) ? "(" + c.getFila() + "," + c.getColumna() + ")" : "(reserva)";
            String oculto = u.isOculto() ? " (OCULTA)" : "";
            
            String linea = String.format("[%d] %-18s %s%s", i + 1, u.getNombre(), pos, oculto);
            System.out.printf("║ %s%-30s%s ║%n", colorLista, linea, Colores.RESET);
        }
        System.out.println("╚══════════════════════════════════╝");

        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null;
        return unidades.get(idx - 1);
    }
    
    public Unidad seleccionarUnidadReserva(List<Unidad> unidades) {
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades en la reserva.");
            return null;
        }
        
        System.out.println("\n╔═══ SELECCIONAR DE RESERVA ═══╗");
        System.out.println("║ [0] Cancelar                 ║");        
        
        for (int i = 0; i < unidades.size(); i++) {
            String linea = String.format("[%d] %s", i + 1, unidades.get(i).getNombre());
            System.out.printf("║ %s%-30s%s ║%n", Colores.ALIADO, linea, Colores.RESET);
        }
        System.out.println("╚══════════════════════════════╝");
                
        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null;
        return unidades.get(idx - 1);
    }

    // Muestra las casillas a las que puede moverse la unidad seleccionada
    public void mostrarCasillasDisponibles(List<Casilla> casillas) {
        if (casillas == null || casillas.isEmpty()) {
            System.out.println("No hay casillas alcanzables para esta unidad.");
            return;
        }

        // Usamos la nueva caja
        System.out.println("\n╔═══ CASILLAS ALCANZABLES ═══╗");
        StringBuilder linea = new StringBuilder("║ ");        
        for (Casilla c : casillas) {
            String coord = String.format("(%d, %d) ", c.getFila(), c.getColumna());
            if (linea.length() + coord.length() > 28) { // Controlar ancho de línea
                System.out.println(linea.toString().trim() + " ║");
                linea = new StringBuilder("║ ");
            }
            linea.append(coord);
        }
        // Imprimir la última línea con padding
        System.out.printf("%s%-29s ║%n", Colores.ALIADO + linea.toString(), Colores.RESET);
        System.out.println("╚══════════════════════════════╝");
    }

    public VistaInicio.Ubicacion pedirUbicacion(String mensaje) {
        System.out.println("-- " + mensaje + " --");
        int fila = leerEntero("Fila");
        int col = leerEntero("Columna");
        return new VistaInicio.Ubicacion(fila, col);
    }

    public void mostrarEmboscadaExitosa(Unidad u) {
        System.out.println("🕵️ " + u.getNombre() + " se ha ocultado en el bosque. ¡El enemigo no podrá verla!");
    }

    public void mostrarEmboscadaInvalida(Unidad u) {
        System.out.println("❌ " + u.getNombre() + " no puede preparar emboscada aquí.");
    }

    // Utilidades de lectura
    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String linea = sc.nextLine();
                return Integer.parseInt(linea.trim());
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
            }
        }
    }

    private int leerEnteroEnRango(String prompt, int min, int max) {//*M* ver forma de unificar esto para todas las vistas
        int valor;
        while (true) {
            valor = leerEntero(prompt);
            if (valor >= min && valor <= max) {
                return valor;
            }
            // Avisa al usuario que el número está fuera de rango
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