package p.dm;

import p.util.Constantes;

import java.util.List;

/**
 * User: JPB
 * Date: Oct 2, 2008
 * Time: 11:23:05 AM
 */
public class GeneradorSalida {

    public static StringBuffer salidaSQL(DataSet ds){
        String s = "INSERT INTO aaaa(ID, NOMBRE, DIRECCION, ALTURA, PISO, DEPTO, LOCALIDAD, " +
                "COD_POSTAL, CIUDAD, CAMPO_10, NOMBRE_2, CAMPO_12, CAMPO_13, CAMPO_14, CAMPO_15, " +
                "CAMPO_16, CAMPO_17, CAMPO_18, CAMPO_19, CAMPO_20, CAMPO_21, CAMPO_22) VALUES( ";
        StringBuffer res = new StringBuffer();

        List<Registro> rgs = ds.getRegistros();
        for(Registro r : rgs) {
            List<Object> atr = r.getAtributos();
            String v = atr2ValuesSQL(atr);
            res.append(s).append(v).append(");").append(Constantes.NUEVA_LINEA);
        }

        return res;
    }

    private static String atr2ValuesSQL(List atrs){
        String res = "";
        for(Object atr : atrs) {
            if (atr != null)
                res += "'" + atr.toString() + "'";
            else
                res += "null";

            res += ", ";
        }
        if (res.endsWith(", "))
            res = res.substring(0, res.length() - 2);

        return res;
    }

    public static StringBuffer salida(DataSet ds, String separador){
        List<String> ns = ds.getNombres();
        StringBuffer res = new  StringBuffer();
        if (ns != null && ns.size() > 0){
            agregar(res, ns, separador);
            res.append(Constantes.NUEVA_LINEA);
        }

        List<Registro> regs = ds.getRegistros();
        for(Registro r : regs) {
            List<Object> atrs = r.getAtributos();
            agregar(res, atrs, separador);
            res.append(Constantes.NUEVA_LINEA);
        }
        return res;
    }

    private static void agregar(StringBuffer sb, List reg, String separador){
        for(Object o : reg) {
            if (o != null)
                sb.append(o.toString());
            sb.append(separador);
        }
    }
}
