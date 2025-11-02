package org.vista.tipos;

import java.util.List;
import java.util.Scanner;
import org.modelo.unidades.Unidad;
import org.vista.TableroRenderer;
import org.modelo.Juego;
import org.modelo.tablero.Tablero;

public class VistaTurno {
    
    private final Scanner in = new Scanner(System.in);

    public void mostrarEstado(Juego juego) {
        System.out.println("\n=== ESTADO DEL JUEGO ===");
        System.out.println("Turno actual: " + juego.getBandoActual());
        
        Tablero tablero = juego.getTablero();
        System.out.println(TableroRenderer.render(tablero));
        
        System.out.println("--- Unidades en Tablero ---");
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
        System.out.println("3. Desplegar unidad (desde reserva)");
        System.out.println("4. Terminar Turno");
        return leerEnteroEnRango("Opción", 1, 4);
    }
    
    public Unidad seleccionarUnidad(List<Unidad> unidades, String prompt) {
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades disponibles para " + prompt + ".");
            return null;
        }
        
        System.out.println("-- Seleccionar Unidad para " + prompt + " --");
        for (int i = 0; i < unidades.size(); i++) {
            Unidad u = unidades.get(i);
            String pos = "(" + u.getCasillaActual().getFila() + "," + u.getCasillaActual().getColumna() + ")";
            System.out.printf("[%d] %s %s%n", i + 1, u.getNombre(), pos);
        }
        System.out.println("[0] Cancelar");
        
        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null;
        return unidades.get(idx - 1);
    }
    
    public Unidad seleccionarUnidadReserva(List<Unidad> unidades) {
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades en la reserva.");
            return null;
        }
        
        System.out.println("-- Seleccionar Unidad a Desplegar --");
        for (int i = 0; i < unidades.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, unidades.get(i).getNombre());
        }
        System.out.println("[0] Cancelar");
        
        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null;
        return unidades.get(idx - 1);
    }

    public VistaInicio.Ubicacion pedirUbicacion(String prompt) {
        System.out.println("-- " + prompt + " --");
        // (Reutiliza la lógica de VistaInicio para pedirUbicacion, idealmente esto estaría en una clase helper)
        int f = leerEnteroEnRango("Fila", 0, 99); // 99 como límite genérico
        int c = leerEnteroEnRango("Columna", 0, 99);
        return new VistaInicio.Ubicacion(f, c);
    }

    private int leerEnteroEnRango(String etiqueta, int min, int max) {
        while (true) {
            System.out.print(etiqueta + " (" + min + "-" + max + "): ");
            try {
                String s = in.nextLine().trim();
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) { }
            System.out.println("Entrada inválida, probá de nuevo.");
        }
    }
}