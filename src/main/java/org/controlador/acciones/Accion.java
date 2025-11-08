package org.controlador.acciones;

// Interfaz de Patron Command
public interface Accion {
    // true si el turno debe continuar, false ecc.
    boolean ejecutar();

    default boolean necesitaPausa() { return true; }
} 
