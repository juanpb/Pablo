package p.proc.pplan;

import p.util.UtilString;

import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 22/04/13
 * Time: 13:53
 */
public class Tranformar {

    public static List<String> cambiarNroLotePP(String nroLote, List<String> pp){
        nroLote = UtilString.rellenarCon(nroLote, "0", 5);
        List<String> res = new ArrayList<String>();

        for (String s : pp) {
            s = s.substring(0, RegPP.LOTE_DESDE )+ nroLote + s.substring(RegPP.LOTE_HASTA);
            res.add(s);
        }
        return res;
    }

    public static List<String> cambiarNroLoteImed(String nroLote, List<String> imed){
        nroLote = UtilString.rellenarCon(nroLote, "0", 8);
        List<String> res = new ArrayList<String>();

        for (String s : imed) {
            s = s.substring(0, RegImed.LOTE_DESDE )+ nroLote + s.substring(RegImed.LOTE_HASTA);
            res.add(s);
        }
        return res;
    }
}
