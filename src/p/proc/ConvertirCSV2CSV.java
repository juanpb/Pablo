package p.proc;

import p.util.UtilFile;

import java.io.File;
import java.util.*;

/**
 * User: JPB
 * Date: Sep 19, 2008
 * Time: 13:13:28 AM
 */
public class ConvertirCSV2CSV {
    private static String ent = "C:\\tmp\\borrar\\Enroll-24092020.csv";
    private static String sal = ent + "_sal.csv";

    public static void main3(String[] args) throws Exception {
        List<String> apl = new ArrayList<String>();
        apl.add("c1,c2,c3,c4");
        apl.add("a1,a2,\"a3,a4\",a5");
        List<String> aplSal = getNuevoFormato(apl);
        print(aplSal);
        System.out.println("listo");
    }

    private static void print(List<String> aplSal) {
        System.out.println("aplSal.size() = " + aplSal.size());
        for (String s : aplSal) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Exception {
        File file = new File(sal);
        file.delete(); //borro salida vieja, si existe

        List<String> apl = UtilFile.getArchivoPorLinea(ent);
        List<String> aplSal = getNuevoFormato(apl);
        UtilFile.guardartArchivoPorLinea(sal, aplSal);
        System.out.println("listo");
    }

    /**
     * Recibe una lista de filas. Una fila puede tener algún campo con n valores. Si tiene n valores => genera n filas
     * de salida, si no una sola. Las filas de salida repiten los campos no únicos y varían los repetidos.
     * Ej: entrada: c1, c2, "a1,a2", c4
     *      salida: fila 1 = c1, c2, a1, c4
     *      salida: fila 2 = c1, c2, a2, c4
     * @param lista
     * @return
     */
    private static List<String> getNuevoFormato(List<String> lista) {
        List<String> res = new ArrayList<String>();

        for (String s : lista) {
            Fila_v2 f = Fila_v2.parse(s);
            res.add(Fila_v2.getN(f, 0)); //siempre se agrega una fila
            int i = 1;
            while (i < f.correos.size() || i < f.telefs.size()) {
                res.add(Fila_v2.getN(f, i++));  //si repite algún campo => genero + filas
            }
        }
        return res;
    }

}

class Fila{
    //ID;Nombre de la cuenta SAM;Nombre de visualización;Dirección de correo ;Número del celular;
    // Nombre de la unidad organizacional;Período inscrito;Última modificación;
    String id;
    String sam;
    String nom;
    List<String> correos;
    List<String> telefs;
    String uo;
    String pi;
    String um;
    static String delim = ";";

    public Fila(String id, String sam, String nom, List<String> correos, List<String> telefs, String uo, String pi, String um) {
        this.id = id;
        this.sam = sam;
        this.nom = nom;
        this.correos = correos;
        this.telefs = telefs;
        this.uo = uo;
        this.pi = pi;
        this.um = um;
    }

    static Fila parse(String f){
        String id = null;
        String sam = null;
        String nom = null;
        List<String> corr = null;
        List<String> telfs = null;
        String uo = null;
        String pi = null;
        String um = null;
        String[] split = f.split(";");
        try {
            int i = 0;
            id = split[i++];
            sam = split[i++];
            nom = split[i++];
            corr = getList(split[i++]);
            String s = split[i++];
            if (s.endsWith(",0")){
                s = s.substring(0, s.length() -2); //si el nro termina con ",0" elimino esos 2 caracteres
            }
            telfs = sacarRepetidos(getList(s));
            uo = split[i++];
            pi = split[i++];
            um = split[i++];
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Línea con problema: ");
            System.out.println(f);
        }

        return new Fila(id, sam, nom, corr, telfs, uo, pi, um);
    }

    private static List<String> sacarRepetidos(List<String> list) {
        Set<String> map = new HashSet<String>();
        for (String s : list) {
            map.add(s.trim());
        }
        List<String> res = new ArrayList<String>();
        Object[] x = map.toArray();
        for (Object o : x) {
            res.add(o.toString());
        }
        return res;
    }


    private static List<String> getList(String s) {
        String delimInt = ",";
        return Arrays.asList(s.split(delimInt));
    }

    //Genera la fila de salida.
    //devuelve una fila con el valor n-ésimo de los campos que pueden tener más de un valor
    public static String getN(Fila f, int n) {
        String res = f.id  + delim;
        res += f.sam + delim;
        res += f.nom + delim;
        String correo = get(f.correos, n);
        correo = vacioXGuion(correo);
        res += correo + delim;
        res += get(f.telefs, n)  + delim;
        res += f.uo + delim;
        res += f.pi + delim;
        res += f.um + delim;

        return res;
    }

    private static String vacioXGuion(String s) {
        if (s.trim().equals(""))
            return "-";
        else
            return s;
    }

    private static String get(List<String> lista, int i) {
        String res = "";
        if (lista.size() <= i ){
            if (lista.size() > 0)
                res = lista.get(0);//devuelvo el primero
        }
        else
            res = lista.get(i);

        if (res.trim().equals(""))
            return "-";
        else
            return res;
    }
}