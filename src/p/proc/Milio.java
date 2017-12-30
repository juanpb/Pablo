package p.proc;

import p.util.UtilFile;

import java.util.List;

/**
 * User: Administrador
 * Date: 25/12/2007
 * Time: 23:17:22
 */
public class Milio {
    String entrada = "C:\\de.txt";

    public static void main(String[] args) throws Exception {
        new Milio().hacer();
    }

    private void hacer() throws Exception {
        List<String> list = UtilFile.getArchivoPorLinea(entrada);
        int cont = 0;
        for (String s : list) {
            if (s != null && s.length() > 0){
                if (s.indexOf(" - ") > 0)
                    cont++;
                else
                    System.out.println(s);
            }
        }
        System.out.println("Discos = " + cont);
    }
}
