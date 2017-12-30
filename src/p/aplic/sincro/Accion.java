package p.aplic.sincro;

import org.apache.log4j.Logger;

/**
 * User: JPB
 * Date: Dec 1, 2008
 * Time: 6:08:23 AM
 */
public class Accion {
    private static final Logger logger = Logger.getLogger(Accion.class);

    /**
     * c -> copia
     * m -> mover
     * mc -> mover contenido
     * a -> abrir
     */
    private String accion;
    private String param1;//origen, programa
    private String param2;//destino
    private String param3;//para verificar

    public Accion() {

    }

    public Accion(String accion, String param1, String param2) {
        this(accion,  param1, param2, null);
    }

    public Accion(String accion, String param1, String param2, String param3) {
        this.accion = accion;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public static Accion crearAccion(String lineaAParsear) {
        //formato: accion "arch o dir origen" "arch o dir origen"

        Accion res = null;
        //noinspection OverlyComplexBooleanExpression
        if (lineaAParsear != null && lineaAParsear.length() > 0
                && !lineaAParsear.startsWith("-")
                && !lineaAParsear.startsWith("set")
                && !lineaAParsear.startsWith("SET")
                && !lineaAParsear.startsWith("//")){
            res = new Accion();

            //busco la acción
            int i = lineaAParsear.indexOf(" ");
            res.setAccion(lineaAParsear.substring(0, i));

            try {
                //busco param1
                int d = lineaAParsear.indexOf("\"");
                int h = lineaAParsear.indexOf("\"", d+1);
                res.setParam1(lineaAParsear.substring(d+1, h));

                //busco la param2
                d = lineaAParsear.indexOf("\"", h+1);
                h = lineaAParsear.indexOf("\"", d+1);
                res.setParam2(lineaAParsear.substring(d+1, h));

                //busco la param3
                d = lineaAParsear.indexOf("\"", h+1);
                if (d > 0){
                    h = lineaAParsear.indexOf("\"", d+1);
                    if (h > 0)
                        res.setParam3(lineaAParsear.substring(d+1, h));
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        
        return  res;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

}
