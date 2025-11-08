// src/main/java/org/vista/tipos/VistaInicio.java
package org.vista.tipos;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.TableroRenderer;

public class VistaInicio {

    public static class Selecciones {
        private final String mapaPath;
        private final String ejercitoPath;

        public Selecciones(String mapaPath, String ejercitoPath) {
            this.mapaPath = mapaPath;
            this.ejercitoPath = ejercitoPath;
        }
        public String getMapaPath() { return mapaPath; }
        public String getEjercitoPath() { return ejercitoPath; }
    }

    public static class Ubicacion {
        private final int fila;
        private final int columna;
        public Ubicacion(int fila, int columna) { this.fila = fila; this.columna = columna; }
        public int getFila() { return fila; }
        public int getColumna() { return columna; }
    }

    private final Scanner in = new Scanner(System.in);

    private static final String DIR_MAPAS = "archivos/mapas";
    private static final String DIR_EJERCITOS = "archivos/ejercito";

public void mostrar() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║                                              ║");
        System.out.println("║         🛡️ BIENVENIDO A CLASS EMBLEM 🛡️        ║");
        System.out.println("║                                              ║");
        System.out.println("╚══════════════════════════════════════════════╝" + Colores.RESET);
        System.out.println();
    }

    public Selecciones seleccionarArchivos() {
        String mapa = pedirArchivo("Mapa", DIR_MAPAS);
        String ejercito = pedirArchivo("Ejército", DIR_EJERCITOS);

        System.out.println("\n╔══════════ CONFIGURACIÓN ══════════╗");
        System.out.printf("║ %-10s %-27s ║%n", "Mapa:", mapa);
        System.out.printf("║ %-10s %-27s ║%n", "Ejército:", ejercito);
        System.out.println("╚══════════════════════════════════════════╝");

        return new Selecciones(mapa, ejercito);
    }

    // --- pide Fila/Columna dentro del rango [0..filas-1], [0..columnas-1] ---
    public Ubicacion pedirUbicacionLord(Bando bando, int filas, int columnas) {

        String bandoColor = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
        String bandoNombre = bando.toString();

        System.out.println("\n╔═════════════ POSICIONAR LORD ═════════════╗");
        System.out.printf("║ Jugador: %s%-31s%s  ║%n", bandoColor, bandoNombre, Colores.RESET);
        System.out.println("║                                           ║");
        System.out.printf("║ Rango Filas:    0..%-22s ║%n", (filas - 1));
        System.out.printf("║ Rango Columnas: 0..%-22s ║%n", (columnas - 1));
        System.out.println("╚═══════════════════════════════════════════╝");

        int f = leerEnteroEnRango("Fila", 0, filas - 1);
        int c = leerEnteroEnRango("Columna", 0, columnas - 1);
        
        return new Ubicacion(f, c);
    }

    public Ubicacion pedirUbicacionUnidad(Unidad unidad, Bando bando, int filas, int columnas) {
        String bandoColor = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
        String bandoNombre = bando.toString();
        String nombreUnidad = unidad.getNombre();

        System.out.println("\n╔════════════ POSICIONAR UNIDAD ════════════╗");
        System.out.printf("║ Jugador: %s%-31s%s  ║%n", bandoColor, bandoNombre, Colores.RESET);

        String lineaUnidad = "Unidad: " + nombreUnidad;
        System.out.printf("║ %-43s ║%n", lineaUnidad);

        System.out.printf("║ Rango Filas:    0..%-22s ║%n", (filas - 1));
        System.out.printf("║ Rango Columnas: 0..%-22s ║%n", (columnas - 1));
        System.out.println("╚═══════════════════════════════════════════╝");

        int f = leerEnteroEnRango("Fila", 0, filas - 1);
        int c = leerEnteroEnRango("Columna", 0, columnas - 1);
        
        return new Ubicacion(f, c);
    }

    // Muestra las unidades en reserva y permite al jugador seleccionar una, o 0 para terminar la fase.
    public Unidad seleccionarUnidadDeReserva(Bando bando, List<Unidad> unidades) {
        if (unidades.isEmpty()) {
            System.out.println("No hay más unidades en la reserva.");
            return null;
        }
        
        String bandoColor = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
        String bandoNombre = bando.toString();
        
        System.out.println("\n╔═══ DESPLEGAR UNIDADES DE RESERVA ═══╗");
        System.out.printf("║ JUGADOR: %s%-24s%s ║%n", bandoColor, bandoNombre, Colores.RESET);
        System.out.println("║─────────────────────────────────────║");
        System.out.println("║ [0] TERMINAR FASE DE DESPLIEGUE     ║");
        
        int i = 1;
        for (Unidad u : unidades) {
            // Mostramos el equipamiento para ayudar a decidir
            String eq = (u.getEquipamiento() != null) ? u.getEquipamiento().getNombre() : "Puño limpio";
            String linea = String.format("[%d] %-15s (%s)", i++, u.getNombre(), eq);
            System.out.printf("║ %s%-35s%s ║%n", bandoColor, linea, Colores.RESET);
        }
        System.out.println("╚═════════════════════════════════════╝");
                
        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null; // [0] TERMINAR FASE
        return unidades.get(idx - 1);
    }

    public boolean pedirConfirmacion(String prompt) {
        while (true) {
            System.out.print(prompt + " (s/n): ");
            String linea = in.nextLine().trim().toLowerCase();
            if (linea.equals("s")) return true;
            if (linea.equals("n")) return false;
            System.out.println(Colores.WARNING + "Respuesta inválida. Intente de nuevo." + Colores.RESET);
        }
    }


    private int leerEnteroEnRango(String etiqueta, int min, int max) {
        while (true) {
            System.out.print(etiqueta + " (" + min + "-" + max + "): ");
            String s = in.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
                System.out.println(Colores.WARNING + "Opción fuera de rango." + Colores.RESET);
            } catch (NumberFormatException ignored) { 
                System.out.println(Colores.WARNING + "Entrada inválida, probá de nuevo." + Colores.RESET);
            }
        }
    }

    // --------- selección de archivos ---------
    private String pedirArchivo(String titulo, String resourceDir) {
        List<String> archivos = listarRecursosCSV(resourceDir);
        if (archivos.isEmpty()) {
            throw new IllegalStateException("No hay CSV en " + resourceDir +
                    " (revisá src/main/resources/" + resourceDir + ")");
        }
        String tituloCaja = String.format(" ELEGIR %S ", titulo.toUpperCase());
        System.out.printf("\n╔═══════════════%s═════════════╗%n", tituloCaja);

        for (int i = 0; i < archivos.size(); i++) {
            String nombre = archivos.get(i).substring(resourceDir.length() + 1);
            String linea = String.format("[%d] %s", i + 1, nombre);
            System.out.printf("║ %-38s  ║%n", linea);
        }
        System.out.println("╚═════════════════════════════════════════╝");

        int idx = leerEnteroEnRango("Opción", 1, archivos.size());
        return archivos.get(idx - 1);
    }

    private List<String> listarRecursosCSV(String resourceDir) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource(resourceDir);
            if (url == null) return List.of();

            if ("file".equals(url.getProtocol())) {
                Path base = Paths.get(url.toURI());
                try (var stream = Files.list(base)) {
                    List<String> out = new ArrayList<>();
                    for (Path p : (Iterable<Path>) stream::iterator) {
                        if (Files.isRegularFile(p) && p.getFileName().toString().toLowerCase().endsWith(".csv")) {
                            out.add(resourceDir + "/" + p.getFileName().toString());
                        }
                    }
                    Collections.sort(out);
                    return out;
                }
            }

            // si no es "file":
            System.out.println("(No puedo listar automáticamente en protocolo " + url.getProtocol() + ")");
            System.out.println("Escribí el nombre del archivo dentro de " + resourceDir + " (ej: mapa1.csv): ");
            String nombre = in.nextLine().trim();
            if (!nombre.toLowerCase().endsWith(".csv")) nombre += ".csv";
            return List.of(resourceDir + "/" + nombre);

        } catch (Exception e) {
            throw new RuntimeException("Error listando resources en " + resourceDir + ": " + e.getMessage(), e);
        }
    }

    public void mostrarTablero(Tablero tablero, Bando bandoActual) {
        System.out.println("\n=== TABLERO ===");
        System.out.println(TableroRenderer.render(tablero, bandoActual));
    }
}
