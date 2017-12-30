package p.proc;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 19/04/12
 * Time: 09:01
 */
public class ContadorLineas  {

    private static String f = "D:\\RootBuild\\_pplan\\paralelo\\v2\\6-may\\imed6may.txt";


    public static void main(String[] args) throws IOException {
        long ini = System.currentTimeMillis();


        LineNumberReader lnr = null;
        int cont = 0;
        try{
            List<String> res = new ArrayList<String>();
            lnr = new LineNumberReader(new FileReader(f));
            while(lnr.ready()){
                lnr.readLine();
                cont++;
            }
        }
        finally{
            if (lnr != null)
                lnr.close();
        }

        System.out.println("cont = " + cont);
        long fin = System.currentTimeMillis();
        long seg = (fin - ini);
        System.out.println("Demoró " + seg + " ms");
    }

 



}
