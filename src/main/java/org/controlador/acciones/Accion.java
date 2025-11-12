package org.controlador.acciones;

public interface Accion {
    boolean ejecutar();

    default boolean necesitaPausa() { return true; }
} 
