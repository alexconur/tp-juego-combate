package org.modelo.tablero;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class FabricaCasillas {

    // *A* arreglé ocp con Singleton
    private static final FabricaCasillas instancia = new FabricaCasillas();
    private static volatile boolean registroListo = false;

    // codigo (por ej LL) -> constructor (fila,col) -> Casilla
    private final Map<String, BiFunction<Integer, Integer, Casilla>> registro;

    private FabricaCasillas() {
        this.registro = new HashMap<>();
    }

    static {
        instancia.inicializarRegistro();
    }

    public static FabricaCasillas getInstancia() {
        return instancia;
    }

    public void registrarTipoCasilla(String codigo, BiFunction<Integer, Integer, Casilla> constructor) {
        if (codigo == null || constructor == null) {
            throw new IllegalArgumentException("El código o el constructor no pueden ser nulos.");
        }
        // normaliza la clave y registra el constructor
        registro.put(codigo.trim().toUpperCase(), constructor);
    }

    // *A* lo explico con comments para que se entienda
    public Casilla crearCasilla(String tipoTerreno, int fila, int columna) {
        // clave sin espacios y en mayusculas
        String tipo = (tipoTerreno == null) ? "" : tipoTerreno.trim().toUpperCase();
        // Busca el constructor registrado para ese tipo
        BiFunction<Integer, Integer, Casilla> ctor = registro.get(tipo);
        // si existe, crea la casilla con (fila,columna)
        if (ctor != null) {
            return ctor.apply(fila, columna);
        }
        throw new IllegalArgumentException("Tipo de terreno [" + tipoTerreno + "] no existe o no está registrado.");
    }

    private void inicializarRegistro() {
        // no deja que se inicialice más de una vez
        if (registroListo) return;
        // usa synchronized para evitar que se inicialice doble (por race condition)
        synchronized (FabricaCasillas.class) {
            if (registroListo) return;
            cargarTiposDeCasilla("org.modelo.tablero.casillas", Casilla.class);
            registroListo = true;
        }
    }

    private static final String paqueteCasillas = "org.modelo.tablero.casillas";
    private static final String rutaCasillas = paqueteCasillas.replace('.', '/');
    // carga todas las clases de casillas del paquete y las registra
    private static void cargarTiposDeCasilla(String paquete, Class<?> superTipo) {
        String ruta = paquete.replace('.', '/');
        // ClassLoader a usar para encontrar y cargar clases
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            // obtiene todas las carpetas del paquete
            Enumeration<URL> recursos = loader.getResources(ruta);
            while (recursos.hasMoreElements()) {
                URL url = recursos.nextElement();
                String protocolo = url.getProtocol();

                if ("file".equals(protocolo)) {
                    // clases en el filesystem
                    Path base = Paths.get(url.toURI());
                    try (var stream = Files.walk(base)) {
                        for (Path p : (Iterable<Path>) stream::iterator) {
                            // filtra archivos .class (no directorios)
                            if (Files.isRegularFile(p) && p.getFileName().toString().endsWith(".class")) {
                                // obtiene la parte del path de la clase (por ej si base=.../casillas y p=.../casillas/Bosque.class → rel="Bosque.class")
                                String rel = base.relativize(p).toString().replace(File.separatorChar, '/');
                                if (rel.contains("$")) continue; // Salta clases internas/anonimas
                                // convierte a nombre de clase. 
                                // ej rel="Bosque.class"  ->  "org.modelo.tablero.casillas.Bosque"
                                String nombreClase = paquete + "." + rel.substring(0, rel.length() - 6).replace('/', '.');
                                // intenta registrar si es una Casilla valida
                                inicializarClaseSiEsCasilla(nombreClase, superTipo, loader);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo inicializar casillas: " + e.getMessage(), e);
        }
    }


    private static final boolean DEBUG_SCANNER = false;
    private static void inicializarClaseSiEsCasilla(String nombreClase, Class<?> superTipo, ClassLoader loader) {
        try {
            Class<?> tipo = Class.forName(nombreClase, false, loader); // no inicializa
            if (superTipo.isAssignableFrom(tipo)) {
                // ahora si inicializa y ejecuta static{} y se registra en la fabrica
                Class.forName(nombreClase, true, loader);
            }
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            // muestra el mensaje si el flag de debug esta encendido
            if (DEBUG_SCANNER) {
                System.err.println(
                    "[FabricaCasillas DEBUG] Advertencia: Se omitió la clase '" + nombreClase + 
                    "'. No se pudo cargar. (Error: " + e.getMessage() + ")"
                );
            }
        }
    }
}