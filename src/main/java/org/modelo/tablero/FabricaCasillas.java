package org.modelo.tablero;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class FabricaCasillas {

    // *A* arreglé ocp con patrón Singleton
    private static final FabricaCasillas instancia = new FabricaCasillas();
    
    // un Map que usa el código (ej: "LL") como clave y un constructor como valor.
    private final Map<String, BiFunction<Integer, Integer, Casilla>> registro;

    private FabricaCasillas() {
        this.registro = new HashMap<>();
    }

    public static FabricaCasillas getInstancia() {
        return instancia;
    }

    public void registrarTipoCasilla(String codigo, BiFunction<Integer, Integer, Casilla> constructor) {
        if (codigo == null || constructor == null) {
            throw new IllegalArgumentException("El código o el constructor no pueden ser nulos.");
        }
        registro.put(codigo.trim().toUpperCase(), constructor);
    }

    public Casilla crearCasilla(String tipoTerreno, int fila, int columna) {
        String tipo = (tipoTerreno == null) ? "" : tipoTerreno.trim().toUpperCase();

        // *A* busca el constructr en el Map
        BiFunction<Integer, Integer, Casilla> constructor = registro.get(tipo);   
        if (constructor != null) {
            return constructor.apply(fila, columna);
        } else {
            throw new IllegalArgumentException("Tipo de terreno [" + tipoTerreno + "] no existe o no está registrado.");
        }
    }
}
