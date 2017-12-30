package p.dm.trans;

import java.math.BigDecimal;
import java.util.List;

/**
 * User: JPB
 * Date: Oct 6, 2008
 * Time: 7:44:57 AM
 */
public class Rango {

    private BigDecimal desde;
    private BigDecimal hasta;
    private Object nuevoValor;

    public Rango(long desde, long hasta, Object nuevoValor) {
        this(new BigDecimal(desde), new BigDecimal(hasta), nuevoValor);
    }

    public Rango(BigDecimal desde, BigDecimal hasta, Object nuevoValor) {
        this.desde = desde;
        this.hasta = hasta;
        this.nuevoValor = nuevoValor;
    }

    public Rango(int d, int h) {
        this(d, h, "CLASE_" + d + "_" + h);
    }

    public boolean enRango(long x){
        return enRango(new BigDecimal(x));
    }
    public boolean enRango(BigDecimal x){
        return desde.doubleValue() - x.doubleValue() <= 0 //desde < x
                && hasta.doubleValue() - x.doubleValue() >=0;// ;
    }

    public BigDecimal getDesde() {
        return desde;
    }

    public void setDesde(BigDecimal desde) {
        this.desde = desde;
    }

    public BigDecimal getHasta() {
        return hasta;
    }

    public void setHasta(BigDecimal hasta) {
        this.hasta = hasta;
    }

    public Object getNuevoValor() {
        return nuevoValor;
    }

    public void setNuevoValor(Object nuevoValor) {
        this.nuevoValor = nuevoValor;
    }

    public String toString() {
        int tam = getHasta().subtract(getDesde()).intValue() + 1;
        return "[" + getDesde() + " - " + getHasta() + "] #("+ tam + ") : ";
    }

    public static String getClasesCVS(List<Rango> rangos){
        String res = "";
        for (Rango rango : rangos) {
            res += rango.getNuevoValor() + ", ";
        }

        if (res.endsWith(", "))
            res = res.substring(0, res.length()-2);
        return res;
    }
}
