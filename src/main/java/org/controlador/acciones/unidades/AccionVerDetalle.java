package org.controlador.acciones.unidades;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.controlador.Colores;
import org.modelo.Juego;
import org.modelo.equipamiento.Equipamiento;
import org.modelo.tablero.Casilla;
import org.modelo.unidades.Unidad;
import org.vista.tipos.VistaUnidades;

public class AccionVerDetalle implements AccionUnidades {
    private final Juego juego;
    private final VistaUnidades vUnidades;

    public AccionVerDetalle(Juego juego, VistaUnidades vUnidades) {
        this.juego = juego;
        this.vUnidades = vUnidades;
    }

    @Override
    public boolean ejecutar() {
        vUnidades.limpiarPantalla();
        List<Unidad> unidades = juego.getTodasUnidadesEnTablero();
        
        List<Unidad> vivas = unidades.stream()
                .filter(Unidad::estaVivo)
                .collect(Collectors.toList());

        if (vivas.isEmpty()) {
            System.out.println("No hay unidades vivas en el tablero.");
            return true;
        }

        List<String> nombres = new ArrayList<>();
        List<String> posiciones = new ArrayList<>();
        List<String> hps = new ArrayList<>();
        List<String> maxHps = new ArrayList<>();
        List<String> estados = new ArrayList<>();
        List<String> colores = new ArrayList<>();

        for (Unidad u : vivas) {
            Casilla c = u.getCasillaActual();
            nombres.add(u.getNombre());
            posiciones.add((c != null) ? "(" + c.getFila() + "," + c.getColumna() + ")" : "(reserva)");
            hps.add(String.valueOf(u.getHp()));
            maxHps.add(String.valueOf(u.getMaxHp()));
            estados.add(u.estaVivo() ? "VIVO" : "MUERTO");
            colores.add(Colores.colorParaBando(u.getBando()));
        }

        int indiceSeleccionado = vUnidades.seleccionarUnidad(nombres, posiciones, hps, maxHps, estados, colores);

        if (indiceSeleccionado == 0) {
            return true;
        }

        Unidad seleccionada = vivas.get(indiceSeleccionado - 1);

        String nombre = seleccionada.getNombre();
        String bando = seleccionada.getBando().toString();
        
        Casilla c = seleccionada.getCasillaActual();
        String pos = (c != null) ? "(" + c.getFila() + "," + c.getColumna() + ")" : "Reserva";
        
        String hp = String.valueOf(seleccionada.getHp());
        String maxHp = String.valueOf(seleccionada.getMaxHp());
        String atk = String.valueOf(seleccionada.getAtkTotal());
        String def = String.valueOf(seleccionada.getDef());
        String mgc = String.valueOf(seleccionada.getMgcTotal());
        String movRestante = String.valueOf(seleccionada.getMovimientoRestante());
        String movTotal = String.valueOf(seleccionada.getMov());

        Equipamiento eq = seleccionada.getEquipamiento();
        String eqNombre = "";
        String eqOfensivo = "";
        String eqRango = "";
        String eqUsos = "";

        if (eq != null) {
            eqNombre = eq.getNombre();
            eqOfensivo = eq.esOfensivo() ? "Sí" : "No";
            eqRango = eq.esOfensivo() ? String.valueOf(eq.getRango()) : "Todos los aliados";
            eqUsos = String.valueOf(eq.getUsosRestantes());
        }

        String estadoCompleto = seleccionada.estaVivo() ? "Vivo" : "Muerto";
        estadoCompleto += seleccionada.isOculto() ? " (Oculto)" : "";
        estadoCompleto += seleccionada.isLord() ? " [LORD]" : "";

        vUnidades.mostrarDetalleUnidad(
            nombre, bando, pos, hp, maxHp, 
            atk, def, mgc, movRestante, movTotal,
            eqNombre, eqOfensivo, eqRango, eqUsos,
            estadoCompleto
        );
        
        return true;
    }
}