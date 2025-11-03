package org.vista.tipos;

import org.modelo.unidades.Unidad;
import org.modelo.equipamiento.Equipamiento;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaUnidades {

    private final Scanner sc = new Scanner(System.in);

    // Menú principal
    public int mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ DE UNIDADES ===");
        System.out.println("0. Volver");
        System.out.println("1. Ver unidades desplegadas");
        System.out.println("2. Ver unidades en reserva");
        System.out.println("3. Ver detalle de una unidad");
        return leerEnteroEnRango("Opción", 0, 3);
    }

    // Mostrar lista de unidades (en tablero o reserva)
    public void mostrarListaUnidades(String titulo, List<Unidad> unidades) {
        System.out.println("\n--- " + titulo + " ---");
        if (unidades.isEmpty()) {
            System.out.println("(vacío)");
            return;
        }

        int i = 1;
        for (Unidad u : unidades) {
            String pos = (u.getCasillaActual() != null)
                    ? "(" + u.getCasillaActual().getFila() + "," + u.getCasillaActual().getColumna() + ")"
                    : "(reserva)";
            String estado = (u.estaVivo() ? "VIVO" : "MUERTO");
            System.out.printf("[%d] %-18s %s  %d/%d HP  [%s]\n",
                    i++, u.getNombre(), pos, u.getHp(), u.getMaxHp(), estado);
        }
        System.out.println("---------------------------");
    }


    // Seleccionar unidad de una lista
    public Unidad seleccionarUnidad(List<Unidad> unidades) {
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades disponibles.");
            return null;
        }

        mostrarListaUnidades("Seleccionar Unidad", unidades);
        int opt = leerEnteroEnRango("Opción (0 para cancelar)", 0, unidades.size());
        return (opt == 0) ? null : unidades.get(opt - 1);
    }

    //Muestra las unidades vivas de una lista y permite seleccionar una
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

        System.out.println("\n=== DETALLE DE UNIDAD ===");
        System.out.println("Nombre: " + u.getNombre());
        System.out.println("Bando: " + u.getBando());
        System.out.println("Posición: " + 
            (u.getCasillaActual() != null 
                ? "(" + u.getCasillaActual().getFila() + "," + u.getCasillaActual().getColumna() + ")" 
                : "Reserva"));
        System.out.println("HP: " + u.getHp() + " / " + u.getMaxHp());
        System.out.println("ATK total: " + u.getAtkTotal());
        System.out.println("DEF total: " + u.getDef());
        System.out.println("MGC total: " + u.getMgcTotal());
        System.out.println("Movimiento restante: " + u.getMovimientoRestante());

        // Mostrar equipamiento
        Equipamiento eq = u.getEquipamiento();
        if (eq != null) {
            System.out.println("Equipamiento: " + eq.getNombre());
            System.out.println("  • Tipo ofensivo: " + (eq.esOfensivo() ? "Sí" : "No"));
            System.out.println("  • Rango: " + eq.getRango());
            System.out.println("  • Usos restantes: " + eq.getUsosRestantes());
        } else {
            System.out.println("Equipamiento: (ninguno)");
        }

        // Estado general
        System.out.println("Estado: " + (u.estaVivo() ? "Vivo" : "Muerto")
                + (u.isOculto() ? " (Oculto)" : "")
                + (u.isLord() ? " [LORD]" : ""));
        System.out.println("==========================\n");
    }

    private int leerEnteroEnRango(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                int val = sc.nextInt();
                if (val >= min && val <= max) return val;
            } catch (InputMismatchException e) {
                sc.nextLine();
            }
            System.out.println("Entrada inválida. Intente nuevamente.");
        }
    }
}
