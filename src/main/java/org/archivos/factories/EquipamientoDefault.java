package org.archivos.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.modelo.equipamiento.Arma;
import org.modelo.equipamiento.Baculo;
import org.modelo.equipamiento.Equipamiento;
import org.modelo.equipamiento.Grimorio;
import org.modelo.equipamiento.TipoArma;
import org.modelo.equipamiento.estrategias.EstrategiaBaculo;

public class EquipamientoDefault implements EquipamientoFactory {

    private final Map<String, Function<String[], Equipamiento>> registro = new HashMap<>();
    
    private final EstrategiaFactory estrategiaFactory;

    private static final int EQ_NOMBRE = 0;
    private static final int EQ_TIPO = 1;
    private static final int EQ_SUBTIPO = 2;
    private static final int EQ_POTENCIA = 3;
    private static final int EQ_ALCANCE = 4;
    private static final int EQ_USOS = 5;

    public EquipamientoDefault(EstrategiaFactory estrategiaFactory) {
        this.estrategiaFactory = estrategiaFactory;
        registrar("ARMA", this::crearArma);
        registrar("GRIMORIO", this::crearGrimorio);
        registrar("BACULO", this::crearBaculo);
    }
    
    private void registrar(String tipo, Function<String[], Equipamiento> creador) {
        registro.put(tipo.toUpperCase(), creador);
    }

    @Override
    public Equipamiento crear(String eqData) {
        if (eqData == null || eqData.isBlank() || !eqData.contains("|")) {
            return null;
        }
        
        String[] partes = eqData.split("\\|");
        String tipoEq = partes[EQ_TIPO].toUpperCase();
        
        Function<String[], Equipamiento> creador = registro.get(tipoEq);
        if (creador == null) {
            throw new IllegalArgumentException("Tipo de equipamiento desconocido: " + tipoEq);
        }
        return creador.apply(partes);
    }
    
    private Arma crearArma(String[] partes) {
        return new Arma(
            partes[EQ_NOMBRE],
            TipoArma.valueOf(partes[EQ_SUBTIPO].toUpperCase()),
            Integer.parseInt(partes[EQ_POTENCIA]),
            Integer.parseInt(partes[EQ_ALCANCE]),
            Integer.parseInt(partes[EQ_USOS])
        );
    }

    private Grimorio crearGrimorio(String[] partes) {
        return new Grimorio(
            partes[EQ_NOMBRE],
            Integer.parseInt(partes[EQ_POTENCIA]),
            Integer.parseInt(partes[EQ_ALCANCE]),
            Integer.parseInt(partes[EQ_USOS])
        );
    }
    
    private Baculo crearBaculo(String[] partes) {
        String nombre = partes[EQ_NOMBRE];
        String subtipo = partes[EQ_SUBTIPO];
        
        EstrategiaBaculo estrategia = this.estrategiaFactory.crear(subtipo);

        int bonusMagia = Integer.parseInt(partes[EQ_POTENCIA]);
        int poderCuracion = Integer.parseInt(partes[EQ_POTENCIA]);
        
        int usos = Integer.parseInt(partes[EQ_USOS]);
        
        return new Baculo(nombre, estrategia, bonusMagia, usos, poderCuracion);
    }
}