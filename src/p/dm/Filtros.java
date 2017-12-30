package p.dm;

import java.util.List;

/**
 * User: JPB
 * Date: Oct 2, 2008
 * Time: 11:32:43 AM
 */
public class Filtros {

    @SuppressWarnings({"ForLoopReplaceableByForEach"})
    public static void sacarComillas(DataSet ds){
        List<Registro> regs = ds.getRegistros();
        for(Registro r : regs) {
            List<Object> atrs = r.getAtributos();
            for(int i = 0; i < atrs.size(); i++) {
                Object o = atrs.get(i);
                if (o != null){
                    String s = o.toString();
                    if (s.startsWith("\"") && s.endsWith("\"")){
                        try {
                            s = s.substring(1, s.length()-1);
                        } catch (Exception e) {
                            System.out.print("Problemas en el trim: ");
                            System.out.println("s = '" + s + "'");
                            System.out.println("en atrs" + atrs.get(0));
                        }
                        atrs.set(i, s);
                    }
                }
            }
        }
    }

    @SuppressWarnings({"ForLoopReplaceableByForEach"})
    public static void trim(DataSet ds){
        List<Registro> regs = ds.getRegistros();
        for(Registro r : regs) {
            List<Object> atrs = r.getAtributos();
            for(int i = 0; i < atrs.size(); i++) {
                Object o = atrs.get(i);
                if (o != null){
                    String s = o.toString().trim();
                    atrs.set(i, s);
                }
            }
        }
    }
}
