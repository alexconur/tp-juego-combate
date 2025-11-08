package org.controlador.acciones.unidades;

public class AccionInvalida implements AccionUnidades {
    @Override
    public boolean ejecutar() {
        System.out.println("Opción inválida.");
        return true;
    }
}