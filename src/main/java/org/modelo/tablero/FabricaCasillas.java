package org.modelo.tablero;

import org.modelo.tablero.casillas.Acantilado;
import org.modelo.tablero.casillas.Agua;
import org.modelo.tablero.casillas.AreaContaminada;
import org.modelo.tablero.casillas.Bosque;
import org.modelo.tablero.casillas.Castillo;
import org.modelo.tablero.casillas.Enredadera;
import org.modelo.tablero.casillas.Llanura;
import org.modelo.tablero.casillas.Pantano;

public class FabricaCasillas {
    public Casilla crearCasilla(String tipoTerreno, int fila, int columna){
        // *A* no me gusta esto de chequear tipos 
        switch (tipoTerreno){
            case "LLANURA": return new Llanura(fila, columna);
            case "BOSQUE": return new Bosque(fila, columna);
            case "PANTANO": return new Pantano(fila, columna);
            case "CASTILLO": return new Castillo(fila, columna);
            case "AREACONT": return new AreaContaminada(fila, columna);
            case "AGUA": return new Agua(fila, columna);
            case "ENREDADERA": return new Enredadera(fila, columna);
            case "ACANTILADO": return new Acantilado(fila, columna);
            default: 
                throw new IllegalArgumentException("Tipo de terreno [" + tipoTerreno + "] no existe");
        }
    }
}
