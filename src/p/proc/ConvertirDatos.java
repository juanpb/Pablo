package p.proc;

import p.util.Constantes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Sep 19, 2008
 * Time: 11:13:28 AM
 */
public class ConvertirDatos {
    public static void main2(String[] args) throws Exception {
        String x = "\"30546771314\",\"MEDICUS SOCIEDAD ANONIMA DE ASISTENCIA MEDICA Y CIENTIFICA                                                                                                      \",\"LARREA              \",\"000877 \",\"06\",\"   \",\"CAPITAL FEDERAL     \",\"1117      \",\"CAPITAL FEDERAL     \",\"20019859070\",\"DE ALL JOSE ANTONIO                                                                                                                                             \",\"URUGUAY             \",\"001055 \",\"  \",\"   \",\"CAPITAL FEDERAL     \",\"1016      \",\"\",\"\",\"M\",\"0805\",,4800.00";
        String s = entrecomillar(x);
        System.out.println("" + s);
    }
    public static void main(String[] args) throws Exception {
        long ini = System.currentTimeMillis();

        String f = "D:\\P\\_env\\DB\\partida\\a.txt0";
        String f2 = "D:\\P\\_env\\DB\\partida\\a_txt0.csv";
//        entrecomillar(new File(f));
        contarCampos(new File(f2));
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos");


        System.out.println("listo");
    }

    public static void contarCampos(File file) throws Exception
    {
        int cont = 0;
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        while(lnr.ready()){
            String str = lnr.readLine();
            int n = contar(str);
            if (n != 23)
                cont++;
        }
        lnr.close();
        System.out.println("cont = " + cont);
        System.out.println("Listo");
    }
    public static void entrecomillar(File file) throws Exception
    {
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        String salida = file.getAbsolutePath().replaceFirst("\\.", "_")+ ".csv";
        File f = new File(salida);
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        while(lnr.ready()){
            String str = lnr.readLine();
            String n = entrecomillar(str);
            n +=Constantes.NUEVA_LINEA;
            fos.write(n.getBytes());
        }
        fos.close();
        lnr.close();
        System.out.println("Listo");
    }

    private static int contar(String str) {
        StringTokenizer st = new StringTokenizer(str,",");
        int cont = 0;
        while (st.hasMoreTokens()) {
            st.nextToken();
            cont++;
        }
        return cont;
    }
    private static String entrecomillar(String str) {
        StringTokenizer st = new StringTokenizer(str,",");
        String n = "";
        while (st.hasMoreTokens()) {
            String s = st.nextToken().trim();
            if (!s.startsWith("\""))
                s = "\"" + s + "\"";
            n += s;
            if(st.hasMoreTokens())
                n += ",";
        }
        return n;
    }
}
