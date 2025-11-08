package org.archivos.factories;
import org.modelo.equipamiento.Equipamiento;

public interface EquipamientoFactory {
    Equipamiento crear(String eqData);
}