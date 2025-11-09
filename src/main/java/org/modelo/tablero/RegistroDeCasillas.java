package org.modelo.tablero;

import org.modelo.tablero.casillas.Acantilado;
import org.modelo.tablero.casillas.Agua;
import org.modelo.tablero.casillas.AreaContaminada;
import org.modelo.tablero.casillas.Bosque;
import org.modelo.tablero.casillas.Castillo;
import org.modelo.tablero.casillas.Enredadera;
import org.modelo.tablero.casillas.Llanura;
import org.modelo.tablero.casillas.Pantano;

public final class RegistroDeCasillas {
    public static void registrarTodo(FabricaCasillas f) {
        f.registrar(Llanura.codigo(), Llanura::new)
         .registrar(Bosque.codigo(), Bosque::new)
         .registrar(Pantano.codigo(), Pantano::new)
         .registrar(Castillo.codigo(), Castillo::new)
         .registrar(Acantilado.codigo(), Acantilado::new)
         .registrar(Agua.codigo(), Agua::new)
         .registrar(Enredadera.codigo(), Enredadera::new)
         .registrar(AreaContaminada.codigo(), AreaContaminada::new);
    }
}