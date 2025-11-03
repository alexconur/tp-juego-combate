package org.vista.tipos;

import org.modelo.Juego;
import org.modelo.unidades.Unidad;
import org.vista.TableroRenderer;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaTurno {
    
    private final Scanner sc = new Scanner(System.in);

    public void mostrarEstado(Juego juego) {
        System.out.println("\n=== ESTADO DEL JUEGO ===");
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("TURNO DE: " + juego.getBandoActual());
        System.out.println("══════════════════════════════════════════════");
        
        Tablero tablero = juego.getTablero();
        System.out.println(TableroRenderer.render(tablero));
        
        System.out.println("\n--- Unidades en Tablero ---");

        for (Unidad u : juego.getTodasUnidadesEnTablero()) {
            String bando = u.getBando().toString().substring(0, 3);
            String pos = (u.getCasillaActual() != null) ? 
                         "(" + u.getCasillaActual().getFila() + "," + u.getCasillaActual().getColumna() + ")" : "(RESERVA)";
            String hp = u.getHp() + "/" + u.getMaxHp() + " HP";
            String estado = u.estaVivo() ? hp : "MUERTO";
            String acciones = "[Mov: " + (u.puedeMoverse() ? "SI" : "NO") + ", Act: " + (u.puedeActuar() ? "SI" : "NO") + "]";
            
            System.out.printf("[%s] %-18s %-10s %s %s%n", bando, u.getNombre(), pos, estado, acciones);
        }
        System.out.println("---------------------------");
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n-- Menú de Acciones --");
        System.out.println("1. Mover unidad");
        System.out.println("2. Atacar / Curar");
        System.out.println("3. Ver unidades / detalles");
        System.out.println("4. Desplegar unidad (desde reserva)");
        System.out.println("5. Terminar Turno");
        return leerEnteroEnRango("Opción", 1, 5);
    }
    
    public Unidad seleccionarUnidad(List<Unidad> unidades, String prompt) {
        if (unidades.isEmpty()) {
            System.out.println("\nNo hay unidades disponibles para " + prompt + ".");
            return null;
        }
        
        System.out.println("\n-- Seleccionar Unidad para " + prompt + " --");
        System.out.println("[0] Cancelar");
        for (int i = 0; i < unidades.size(); i++) {
            Unidad u = unidades.get(i);
            Casilla c = u.getCasillaActual();
            String pos = (c != null) ? "(" + c.getFila() + "," + c.getColumna() + ")" : "(reserva)";
            System.out.printf("[%d] %-18s %s\n", i + 1, u.getNombre(), pos);
        }
        
        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null;
        return unidades.get(idx - 1);
    }
    
    public Unidad seleccionarUnidadReserva(List<Unidad> unidades) {
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades en la reserva.");
            return null;
        }
        
        System.out.println("\n-- Seleccionar Unidad a Desplegar --");
        System.out.println("[0] Cancelar");
        for (int i = 0; i < unidades.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, unidades.get(i).getNombre());
        }
        
        
        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null;
        return unidades.get(idx - 1);
    }

    public VistaInicio.Ubicacion pedirUbicacion(String mensaje) {
        System.out.println("-- " + mensaje + " --");
        int fila = leerEntero("Fila");
        int col = leerEntero("Columna");
        return new VistaInicio.Ubicacion(fila, col);
    }

    // Utilidades de lectura
    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Entrada inválida. Intente de nuevo.");
            }
        }
    }

    private int leerEnteroEnRango(String prompt, int min, int max) {  //*M* ver forma de unificar esto para todas las vistas
        int valor;
        do {
            valor = leerEntero(prompt);
        } while (valor < min || valor > max);
        return valor;
    }

}