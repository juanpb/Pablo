package p.pruebas.covertchanneltp;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: 08/05/17
 * Time: 11:40
 */
public class Util {
    public static Font font = new Font("SansSerif", Font.PLAIN, 80);
    public static Dimension dimension = new Dimension(1030, 150);



    public static void main(String[] args) {
        //Ejemplo de binary to ascii
//        String input = "1110100";
//        char c = binary2ascii(input);

        for (int i = 0; i < 256; i++) {
            char c =  (char)i;
            String b = char2binary(c);
            char a = binary2ascii(b);
            System.out.println(i + "-" + c + "-" + a + "-" + b);
        }
    }

    public static char binary2ascii(String binary){
        int charCode = Integer.parseInt(binary, 2);
        return (char)charCode;
    }

    public static String char2binary(char c){
        String s = Integer.toBinaryString(c);

        //agrego los ceros a la izquierda
        while (s.length() < 8) {
            s = "0"+s;
        }
        return s;
    }

//    /**
//     * Si el archivo destino no existe => lo crea
//     * @param txt
//     * @param destino
//     */
//    public static void agregarAlFinal(String txt, File destino)
//            throws IOException {
//        if (destino.exists())
//            txt = Constantes.NUEVA_LINEA + txt;
//        else
//            destino.createNewFile();
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(destino, true);
//            fos.write(txt.getBytes());
//            fos.close();
//        }catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//            throw ex;
//        }
//        finally{
//            if (fos != null)
//                fos.close();
//        }
//    }


    public static List<String> getArchivoPorLinea(File f)
            throws IOException{
        return getArchivoPorLinea(f, 0);
    }

    public static List<String> getArchivoPorLinea(File f, int empezarDesdeLineaNro)
            throws IOException{
        LineNumberReader lnr = null;
        int cont = 0;
        try{
            List<String> res = new ArrayList<String>();
            lnr = new LineNumberReader(new FileReader(f));
            while(lnr.ready() && cont >= empezarDesdeLineaNro){
                String linea = lnr.readLine();
                if (linea.endsWith("\t"))//solo guarda lás líneas que terminan con TAB
                    res.add(linea);
                cont++;
            }
            return res;
        }
        finally{
            if (lnr != null)
                lnr.close();
        }
    }

}
