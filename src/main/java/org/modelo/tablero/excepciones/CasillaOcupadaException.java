package org.modelo.tablero.excepciones;

public class CasillaOcupadaException extends RuntimeException {
    public CasillaOcupadaException() {
        super("La casilla ya se encuentra ocupada.");
    }
    public CasillaOcupadaException(String mensaje) {
        super(mensaje);
    }
}