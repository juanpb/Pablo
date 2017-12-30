package p.gui.systemTray;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * User: JPB
 * Date: Sep 1, 2008
 * Time: 12:42:42 PM
 */
public class Programa {
    private static final Logger logger = Logger.getLogger(Programa.class);
    private String nombre;
    private String archivoIni;
    private String clase;

    public Programa(String nombre, String clase, String archivoIni) {
        this.nombre = nombre;
        this.archivoIni = archivoIni;
        this.clase = clase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArchivoIni() {
        return archivoIni;
    }

    public void setArchivoIni(String archivoIni) {
        this.archivoIni = archivoIni;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public void ejecutar(){
        try {
            Class<?> c = Class.forName(getClase());
            Method m = c.getMethod("main", String[].class);
            String ini = getArchivoIni();
            Object[] args = new String[0];
            if (ini != null){
                args = new String[1];
                args[0] = ini;
            }
            System.out.println("getClase() = " + getClase());
            System.out.println("getArchivoIni() = " + getArchivoIni());
            Object o = m.invoke(c, args);
            System.out.println("o = " + o);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
