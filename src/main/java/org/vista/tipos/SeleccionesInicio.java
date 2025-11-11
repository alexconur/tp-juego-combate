package org.vista.tipos;

public class SeleccionesInicio {
    private final String mapaPath;
    private final String ejercitoPath;

    public SeleccionesInicio(String mapaPath, String ejercitoPath) {
        this.mapaPath = mapaPath;
        this.ejercitoPath = ejercitoPath;
    }
    public String getMapaPath() { return mapaPath; }
    public String getEjercitoPath() { return ejercitoPath; }
}
