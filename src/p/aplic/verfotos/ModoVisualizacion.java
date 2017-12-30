package p.aplic.verfotos;

/**
 * User: JPB
* Date: Jun 19, 2008
* Time: 3:37:46 PM
*/
public enum ModoVisualizacion {
    TAMANO_REAL_A_PANTALLA, PANTALLA_A_TAMANO_REAL,
    TAMANO_REAL_A_PANTALLA_A_REAL, AJUSTAR_A_ANCHO, AJUSTAR_A_PANTALLA;

    public static ModoVisualizacion parse(String s){
        if (s == null)
            return null;
        s = s.toLowerCase();
        if ("TAMANO_REAL_A_PANTALLA".toLowerCase().equals(s))
            return TAMANO_REAL_A_PANTALLA;
        else if ("PANTALLA_A_TAMANO_REAL".toLowerCase().equals(s))
            return PANTALLA_A_TAMANO_REAL;
        else if ("TAMANO_REAL_A_PANTALLA_A_REAL".toLowerCase().equals(s))
            return TAMANO_REAL_A_PANTALLA_A_REAL;
        else if ("AJUSTAR_A_ANCHO".toLowerCase().equals(s))
            return AJUSTAR_A_ANCHO;
        else if ("AJUSTAR_A_PANTALLA".toLowerCase().equals(s))
            return AJUSTAR_A_PANTALLA;
        else
            return null;
    }
}
