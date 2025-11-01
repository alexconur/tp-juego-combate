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
import org.vista.TableroRenderer;

public class VistaInicio {

    public static class Selecciones {
        private final String mapaPath;
        private final String ejercitoPath;
        private final String arsenalPath;

        public Selecciones(String mapaPath, String ejercitoPath, String arsenalPath) {
            this.mapaPath = mapaPath;
            this.ejercitoPath = ejercitoPath;
            this.arsenalPath = arsenalPath;
        }
        public String getMapaPath()     { return mapaPath; }
        public String getEjercitoPath() { return ejercitoPath; }
        public String getArsenalPath()  { return arsenalPath; }
    }
    public static class Ubicacion {
        private final int fila;
        private final int columna;
        public Ubicacion(int fila, int columna) { this.fila = fila; this.columna = columna; }
        public int getFila() { return fila; }
        public int getColumna() { return columna; }
    }

    private final Scanner in = new Scanner(System.in);

    private static final String DIR_MAPAS     = "archivos/mapas";
    private static final String DIR_EJERCITOS = "archivos/ejercito";
    private static final String DIR_ARSENAL   = "archivos/arsenal";

    public void mostrar() {
        System.out.println("\n| Bienvenido a CLASS EMBLEM |\n");
    }

    public Selecciones seleccionarArchivos() {
        String mapa     = pedirArchivo("Mapa",     DIR_MAPAS);
        String ejercito = pedirArchivo("Ejército", DIR_EJERCITOS);
        String arsenal  = pedirArchivo("Arsenal",  DIR_ARSENAL);

        System.out.println("\n✔ Selecciones:");
        System.out.println(" - Mapa:     " + mapa);
        System.out.println(" - Ejército: " + ejercito);
        System.out.println(" - Arsenal:  " + arsenal);

        return new Selecciones(mapa, ejercito, arsenal);
    }

    // --- pide Fila/Columna dentro del rango [0..filas-1], [0..columnas-1] ---
    public Ubicacion pedirUbicacionLord(int filas, int columnas) {
        System.out.println("\n-- Ubicar Lord --");
        System.out.println("Usá índices base 0. Rango válido: filas 0.." + (filas - 1) +
                           ", columnas 0.." + (columnas - 1) + ".");
        int f = leerEnteroEnRango("Fila", 0, filas - 1);
        int c = leerEnteroEnRango("Columna", 0, columnas - 1);
        System.out.println("✔ Lord ubicado tentativamente en (" + f + "," + c + ")");
        return new Ubicacion(f, c);
    }

    private int leerEnteroEnRango(String etiqueta, int min, int max) {
        while (true) {
            System.out.print(etiqueta + " (" + min + "-" + max + "): ");
            String s = in.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) { }
            System.out.println("Entrada inválida, probá de nuevo.");
        }
    }

    // --------- selección de archivos ---------
    private String pedirArchivo(String titulo, String resourceDir) {
        List<String> archivos = listarRecursosCSV(resourceDir);
        if (archivos.isEmpty()) {
            throw new IllegalStateException("No hay CSV en " + resourceDir +
                    " (revisá src/main/resources/" + resourceDir + ")");
        }
        System.out.println("-- Elegir " + titulo + " --");
        for (int i = 0; i < archivos.size(); i++) {
            String nombre = archivos.get(i).substring(resourceDir.length() + 1);
            System.out.printf("[%d] %s%n", i + 1, nombre);
        }
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

    public void mostrarTablero(Tablero tablero) {
        System.out.println("\n=== TABLERO ===");
        System.out.println(TableroRenderer.render(tablero));
    }
}
