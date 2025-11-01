package org.modelo.tablero.excepciones;

// Excepción que se lanza al intentar ocupar una casilla que ya tiene una unidad.
public class CasillaOcupadaException extends RuntimeException {
    public CasillaOcupadaException() {
        super("La casilla ya se encuentra ocupada.");
    }
    public CasillaOcupadaException(String mensaje) {
        super(mensaje);
    }
}