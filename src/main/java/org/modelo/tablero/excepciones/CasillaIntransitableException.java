package org.modelo.tablero.excepciones;

// Excepción que se lanza al intentar ocupar una casilla que no es transitable.
public class CasillaIntransitableException extends RuntimeException {
    public CasillaIntransitableException() {
        super("La casilla es intransitable.");
    }
    public CasillaIntransitableException(String mensaje) {
        super(mensaje);
    }
}