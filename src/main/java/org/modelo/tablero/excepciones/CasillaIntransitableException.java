package org.modelo.tablero.excepciones;

public class CasillaIntransitableException extends RuntimeException {
    public CasillaIntransitableException() {
        super("La casilla es intransitable.");
    }
    public CasillaIntransitableException(String mensaje) {
        super(mensaje);
    }
}