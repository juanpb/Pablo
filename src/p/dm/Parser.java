package p.dm;

import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Oct 2, 2008
 * Time: 10:40:33 AM
 */
public class Parser {

    private static String separador = ",";
    private static int cant_max = Integer.MAX_VALUE;

    public static DataSet parsear(String file) throws IOException {
        return parsear(new File(file));
    }

    public static DataSet parsear(File file) throws IOException {
        List<String> lista = UtilFile.getArchivoPorLinea(file, cant_max);
        DataSet res = new DataSet();
        for(String s : lista) {
            if (s.startsWith("@")|| s.startsWith("%")){
                res.addOtraLinea(s);
            }
            else{
                StringTokenizer st = new StringTokenizer(s, separador);
                Registro reg = token2Registro(st);
                res.add(reg);
            }
        }
        return res;    
    }

    /**
     * Graba los registros en el archivo recibido como parámetro. Si existe => agrega al nombre "_CONTADOR"
     * @param ds
     * @param file
     * @return
     * @throws IOException
     */
    public static String grabar(DataSet ds, File file) throws IOException {
        List<Registro> registros = ds.getRegistros();
        List<String> otros = ds.getOtrasLineas();
        if (file.exists()){
            String name = file.getName();
            String parent = file.getParent();
            int cont=0;
            while (file.exists()){
                file = new File(parent,  name + "_" + cont++);
            }
        }
        List<String> datos = new ArrayList<String>();
        for (String r : otros) {
            datos.add(r);
        }
        for (Registro r : registros) {
            datos.add(r.toString(separador));
        }

        UtilFile.guardartArchivoPorLinea(file, datos, false);

        return file.getAbsolutePath();
    }

    private static Registro token2Registro(StringTokenizer st){
        Registro reg = new Registro();

        while(st.hasMoreTokens()) {
            reg.add(st.nextToken());
        }

        return reg;
    }
}
