package org.vista.tipos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.modelo.Juego;
import org.modelo.tablero.Casilla;
import org.modelo.tablero.Tablero;
import org.modelo.unidades.Bando;
import org.modelo.unidades.Unidad;
import org.vista.Colores;
import org.vista.TableroRenderer;

public class VistaTurno {
    
    private final Scanner sc = new Scanner(System.in);

    public void mostrarEstado(Juego juego) {
        if (juego.isGameOver()) {
            return;
        }
        System.out.println("\n══════════════════════════════════════════════");

        String bandoColor = (juego.getBandoActual() == org.modelo.unidades.Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;

        System.out.println("TURNO NÚMERO: " + juego.getNumeroTurno());
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
                colorBando = (u.getBando() ==  Bando.REINO_DRUIDA) ? Colores.DRUIDA : Colores.NIGROMANTICO;
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
        System.out.println("\n╔═════════════════════════ ACCIONES ═════════════════════════════════╗");
        System.out.println("║ [1] Mover           [2] Atacar/Curar      [3] Ver unidades         ║");
        System.out.println("║ [4] Desplegar       [5] Emboscada         [6] Informacion casillas ║");
        System.out.println("║ [7] Terminar turno  [8] Rendirse                                   ║");        
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");   
        
        return leerEnteroEnRango("Opción", 1, 8);
    }
    
    public Unidad seleccionarUnidad(List<Unidad> unidades, String prompt, Juego juego) {
        if (unidades.isEmpty()) {
            System.out.println("\nNo hay unidades disponibles para " + prompt + ".");
            return null;
        }
        
        System.out.println("\n╔═══ SELECCIONAR UNIDAD (" + prompt.toUpperCase() + ") ═══╗");
        System.out.println("║ [0] Cancelar                   ║");

        // Colorear la lista de selección
        Bando bandoActual = juego.getBandoActual();
        Bando bandoEnemigo = (bandoActual == Bando.REINO_DRUIDA) ? Bando.REINO_NIGROMANTICO : Bando.REINO_DRUIDA;
        String colorLista = Colores.colorParaBando(bandoActual); // Default a Aliado
        if (!unidades.isEmpty() && unidades.get(0).getBando() != org.modelo.unidades.Bando.REINO_DRUIDA) { // Asunción simple
             if(prompt.equalsIgnoreCase("Atacar")) {
                colorLista = Colores.colorParaBando(bandoEnemigo);
             }
        }
        if(prompt.equalsIgnoreCase("Atacar")) colorLista = Colores.colorParaBando(bandoEnemigo);
        else if(prompt.equalsIgnoreCase("Curar") || prompt.equalsIgnoreCase("Mover")) colorLista = Colores.colorParaBando(bandoActual);

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
    
    public Unidad seleccionarUnidadReserva(List<Unidad> unidades, Juego juego) {
        if (unidades.isEmpty()) {
            System.out.println("No hay unidades en la reserva.");
            return null;
        }
        
        System.out.println("\n╔═══ SELECCIONAR DE RESERVA ═══╗");
        System.out.println("║ [0] Cancelar                 ║");        
        
        for (int i = 0; i < unidades.size(); i++) {
            String linea = String.format("[%d] %s", i + 1, unidades.get(i).getNombre());
            System.out.printf("║ %s%-30s%s ║%n", Colores.colorParaBando(juego.getBandoActual()), linea, Colores.RESET);
        }
        System.out.println("╚══════════════════════════════╝");
                
        int idx = leerEnteroEnRango("Opción", 0, unidades.size());
        if (idx == 0) return null;
        return unidades.get(idx - 1);
    }

    // Muestra las casillas a las que puede moverse la unidad seleccionada
    public void mostrarCasillasDisponibles(List<Casilla> casillas,Tablero tablero, Bando bandoActual) {
        if (casillas == null || casillas.isEmpty()) {
            System.out.println("No hay casillas alcanzables para esta unidad.");
            return;
        }

        // Usamos la nueva caja
        System.out.println("\n═════════ CASILLAS ALCANZABLES ═════════");
        System.out.println(TableroRenderer.render(tablero, bandoActual, casillas));
        System.out.println("═════════════════════════════════════════");
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
            } catch (InputMismatchException | NumberFormatException e) {
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

    public void mostrarInfoCasillas(){
        final String RESET = Colores.RESET;
        // ancho fijo del bloque visual (3 espacios coloreados)
        String bloqueBosque  = Colores.TERRENO_BOSQUE_BG  + "   " + RESET;
        String bloqueLlanura = Colores.TERRENO_LLANURA_BG + "   " + RESET;
        String bloquePantano = Colores.TERRENO_PANTANO_BG + "   " + RESET;
        String bloqueCastillo= Colores.TERRENO_CASTILLO_BG + "   " + RESET;
        String bloqueAgua    = Colores.TERRENO_AGUA_BG    + "   " + RESET;
        String bloqueAguaCont = Colores.TERRENO_PELIGROSO_BG + "   " + RESET;
        String bloqueAcantilado = Colores.TERRENO_ACANTILADO_BG + "   " + RESET;
        String bloqueEnredadera = Colores.TERRENO_ENREDADERA_BG + "   " + RESET;

        StringBuilder sb = new StringBuilder();
        sb.append("\n╔═══════════════════════ INFORMACIÓN DE CASILLAS ═══════════════════════╗\n");

        // cada línea usa el bloque coloreado seguido de la descripción; formateo para alineación
        sb.append(String.format("║ %s  Bosque: Aumenta 5 ATK y 5 MGC, permite emboscadas.               ║%n", bloqueBosque));
        sb.append(String.format("║ %s  Llanura: Terreno abierto.                                        ║%n", bloqueLlanura));
        sb.append(String.format("║ %s  Pantano: Reduce movimiento al minimo.                            ║%n", bloquePantano));
        sb.append(String.format("║ %s  Castillo: Restaura 10 HP y aumenta DEF en 5.                     ║%n", bloqueCastillo));
        sb.append(String.format("║ %s  Agua: no se puede atravesar.                                     ║%n", bloqueAgua));
        sb.append(String.format("║ %s  Área contaminada: reduce 5 HP al final del turno.                ║%n", bloqueAguaCont));
        sb.append(String.format("║ %s  Acantilado: no se puede avanzar.                                 ║%n", bloqueAcantilado));
        sb.append(String.format("║ %s  Enredadera: no se puede recorrer.                                ║%n", bloqueEnredadera));
        sb.append("╚═══════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(sb.toString());
        System.out.println("Presione Enter para continuar...");
        sc.nextLine();
    }
}