package org.modelo.tablero;

public class InfoCasilla {
    private final String nombre;
    private final String color;
    private final String descripcion;

    public InfoCasilla(Casilla casilla) {
        this.nombre = casilla.getTipoTerreno();
        this.color = casilla.getCodigoColorVista();
        this.descripcion = casilla.descripcionEfecto();
    }

    public String getNombre() { return nombre; }
    public String getColor() { return color; }
    public String getDescripcion() { return descripcion; }
}
