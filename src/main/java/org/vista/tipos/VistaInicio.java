package org.vista.tipos;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.controlador.Colores;
import org.modelo.unidades.Bando;

public class VistaInicio {

    private final Scanner in = new Scanner(System.in);

    private static final String DIR_MAPAS = "archivos/mapas";
    private static final String DIR_EJERCITOS = "archivos/ejercito";

    public void mostrar() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║                                              ║");
        System.out.println("║         🛡️  BIENVENIDO A CLASS EMBLEM 🛡️       ║");
        System.out.println("║                                              ║");
        System.out.println("╚══════════════════════════════════════════════╝" + Colores.RESET);
        System.out.println();
    }

    public SeleccionesInicio seleccionarArchivos() {
        String mapa = pedirArchivo("Mapa", DIR_MAPAS);
        String ejercito = pedirArchivo("Ejército", DIR_EJERCITOS);

        List<String> config = new ArrayList<>();
        config.add("Mapa:      " + mapa);
        config.add("Ejército:  " + ejercito);

        imprimirCajaCompleta("Configuración", config);
        return new SeleccionesInicio(mapa, ejercito);
    }

    public UbicacionInicio pedirUbicacionLord(Bando bando, int filas, int columnas) {

        String color = (bando == Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;

        List<String> lineas = new ArrayList<>();
        lineas.add("Jugador: " + color + bando + Colores.RESET);
        lineas.add("─"); 
        lineas.add("Rango Filas:    0.." + (filas - 1));
        lineas.add("Rango Columnas: 0.." + (columnas - 1));

        imprimirCajaCompleta("Posicionar Lord", lineas);

        int f = leerEnteroEnRango("Fila", 0, filas - 1);
        int c = leerEnteroEnRango("Columna", 0, columnas - 1);

        return new UbicacionInicio(f, c);
    }

    public UbicacionInicio pedirUbicacionUnidad(String nombreUnidad, String bandoNombre, String bandoColor, int filas, int columnas) {

        List<String> info = new ArrayList<>();
        info.add("Jugador: " + bandoColor + bandoNombre + Colores.RESET);
        info.add("Unidad: " + nombreUnidad);
        info.add("Rango Filas:    0.." + (filas - 1));
        info.add("Rango Columnas: 0.." + (columnas - 1));

        imprimirCajaOpciones("Posicionar Unidad", info);

        int f = leerEnteroEnRango("Fila", 0, filas - 1);
        int c = leerEnteroEnRango("Columna", 0, columnas - 1);

        return new UbicacionInicio(f, c);
    }

    public int seleccionarUnidadDeReserva(String bandoNombre, String bandoColor, List<String> nombresUnidades, List<String> equipsUnidades) {

        List<String> lineas = new ArrayList<>();

        lineas.add("Jugador: " + bandoColor + bandoNombre + Colores.RESET);
        lineas.add("─");
        lineas.add("[0] TERMINAR FASE DE DESPLIEGUE");

        for (int i = 0; i < nombresUnidades.size(); i++) {
            String nombre = nombresUnidades.get(i);
            String equip  = equipsUnidades.get(i);

            lineas.add(String.format("[%d] %s%s (%s)%s", i + 1, bandoColor, nombre, equip, Colores.RESET));    
        }

        imprimirCajaConBloques("Desplegar Unidades de Reserva", lineas);

        int idx = leerEnteroEnRango("Opción", 0, nombresUnidades.size());
    
        return idx;
    }

    public boolean pedirConfirmacion(String prompt) {
        while (true) {
            System.out.print(prompt + " (s/n): ");
            String linea = in.nextLine().trim().toLowerCase();
            if (linea.equals("s")) return true;
            if (linea.equals("n")) return false;
            System.out.println(Colores.WARNING + "Respuesta inválida." + Colores.RESET);
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

    private String pedirArchivo(String titulo, String resourceDir) {
        List<String> archivos = listarRecursosCSV(resourceDir);

        if (archivos.isEmpty()) {
            throw new IllegalStateException("No hay CSV en " + resourceDir +
                    " (revisá src/main/resources/" + resourceDir + ")");
        }

        List<String> opciones = new ArrayList<>();
        for (int i = 0; i < archivos.size(); i++) {
            opciones.add(String.format("[%d] %s",
                    i + 1,
                    archivos.get(i).substring(resourceDir.length() + 1)));
        }

        imprimirCajaOpciones("Elegir " + titulo, opciones);

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

            System.out.println("(No puedo listar automáticamente en protocolo " + url.getProtocol() + ")");
            System.out.println("Escribí el nombre del archivo dentro de " + resourceDir + " (ej: mapa1.csv): ");
            String nombre = in.nextLine().trim();

            if (!nombre.toLowerCase().endsWith(".csv")) nombre += ".csv";
            return List.of(resourceDir + "/" + nombre);

        } catch (Exception e) {
            throw new RuntimeException("Error listando resources en " + resourceDir + ": " + e.getMessage(), e);
        }
    }

    public void mostrarTablero(String tableroRenderizado) {
        System.out.println("\n═══════════════ TABLERO ═══════════════");
        System.out.println(tableroRenderizado);
    }

    private void imprimirCajaOpciones(String titulo, List<String> lineas) {
        String tituloTxt = " " + titulo.toUpperCase() + " ";

        int ancho = tituloTxt.length();
        for (String l : lineas) {
            ancho = Math.max(ancho, limpiarANSI(l).length());
        }

        String arriba = "╔" + "═".repeat(ancho) + "╗";
        String titul = "║" + centrar(tituloTxt, ancho) + "║";
        String sep = "║" + "─".repeat(ancho) + "║";
        String abajo = "╚" + "═".repeat(ancho) + "╝";

        System.out.println();
        System.out.println(arriba);
        System.out.println(titul);
        System.out.println(sep);

        for (String l : lineas) {
            String clean = limpiarANSI(l);
            System.out.println("║" + l + " ".repeat(ancho - clean.length()) + "║");
        }

        System.out.println(abajo);
    }

    private void imprimirCajaConBloques(String titulo, List<String> lineas) {
        String tituloTxt = " " + titulo.toUpperCase() + " ";

        int ancho = tituloTxt.length();
        for (String l : lineas) {
            if (!l.equals("─"))
                ancho = Math.max(ancho, limpiarANSI(l).length());
        }

        String arriba = "╔" + "═".repeat(ancho) + "╗";
        String titul = "║" + centrar(tituloTxt, ancho) + "║";
        String sep = "║" + "─".repeat(ancho) + "║";
        String abajo = "╚" + "═".repeat(ancho) + "╝";

        System.out.println();
        System.out.println(arriba);
        System.out.println(titul);
        System.out.println(sep);

        for (String l : lineas) {
            if (l.equals("─")) {
                System.out.println(sep);
                continue;
            }

            String limpio = limpiarANSI(l);
            System.out.println("║" + l + " ".repeat(ancho - limpio.length()) + "║");
        }

        System.out.println(abajo);
    }

    private void imprimirCajaCompleta(String titulo, List<String> lineas) {
        String tituloTxt = " " + titulo.toUpperCase() + " ";

        int ancho = tituloTxt.length();
        for (String l : lineas) {
            if (!l.equals("─"))
                ancho = Math.max(ancho, limpiarANSI(l).length());
        }

        String arriba    = "╔" + "═".repeat(ancho) + "╗";
        String titul  = "║" + centrar(tituloTxt, ancho) + "║";
        String sep    = "╠" + "═".repeat(ancho) + "╣";
        String abajo = "╚" + "═".repeat(ancho) + "╝";

        System.out.println();
        System.out.println(arriba);
        System.out.println(titul);
        System.out.println(sep);

        for (String l : lineas) {

            if (l.equals("─")) {
                System.out.println(sep);
                continue;
            }

            String limpio = limpiarANSI(l);
            System.out.println("║" + l + " ".repeat(ancho - limpio.length()) + "║");
        }

        System.out.println(abajo);
    }

    private String limpiarANSI(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    private String centrar(String s, int ancho) {
        int espacios = ancho - s.length();
        int izq = espacios / 2;
        int der = espacios - izq;
        return " ".repeat(izq) + s + " ".repeat(der);
    }
}
