package p.dm.trans;

import p.dm.Registro;

import java.math.BigDecimal;
import java.util.List;

/**
 * User: JPB
 * Date: Oct 6, 2008
 * Time: 7:59:09 AM
 */
public class AplicarRango {

    public static void aplicar(List<Registro> list, List<Rango> rangos, int atrib){
        for(Registro r : list) {
            Object v = r.getAtributos().get(atrib);
            v = nuevoValor(v, rangos);
            r.getAtributos().set(atrib, v);
        }
    }

    private static Object nuevoValor(Object v, List<Rango> rangos){
        BigDecimal b = new BigDecimal(v.toString());
        for(Rango rango : rangos) {
            if (rango.enRango(b))
                return rango.getNuevoValor();
        }
        return "NO ENCONTRÓ RANGO ( " + v + ")";
    }
}
