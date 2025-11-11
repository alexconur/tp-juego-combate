package org.modelo.tablero.excepciones;

public class DespliegueException extends RuntimeException {
    public DespliegueException(String mensaje) {
        super(mensaje);
    }
}