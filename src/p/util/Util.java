package p.util;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.text.Collator;
import java.util.*;
import java.util.List;


public class Util {
    private static final Logger logger = Logger.getLogger(Util.class);

    public Util() {
    }

    public static Locale getLocaleArgentina(){
        return new Locale("es", "AR", "Traditional_WIN");
    }

    public static void bloquearPC(){
        String x = "rundll32.exe user32.dll, LockWorkStation";
        //por lo menos, funciona con win2000
        try {
            Runtime.getRuntime().exec(x);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static String ordenar(String pTxt){
        return ordenar(pTxt, "\n");
    }

     public static <T> T[] ordenar(List<T> pVec){
        T[] arreglo = (T[]) pVec.toArray();
        return  ordenar(arreglo);
    }

     public static <T> List<T> ordenarList(List<T> pVec){
         T[] arreglo = ordenar(pVec);
         List<T> res = new ArrayList<T>();
         for(T t : arreglo) {
             res.add(t);
         }
        return  res;
    }

    public static <T> T[] ordenar(T[]  arreglo){
        Collator collator = Collator.getInstance(getLocaleArgentina());
        Arrays.sort(arreglo, collator);
        return arreglo;
    }

    public static Object[] ordenar(Object[]  arreglo, Comparator comparator){
        Arrays.sort(arreglo, comparator);
        return arreglo;
    }

    public static void ordenar(String[] pAOrdenar){
        Collator collator = Collator.getInstance(getLocaleArgentina());
        Arrays.sort(pAOrdenar, collator);
    }

    public static String ordenar(File file){
        String res = "";
        List<String> vecTem = new ArrayList<String>();
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            while(lnr.ready()){
                String linea = lnr.readLine();
                if (linea != null && !linea.equals(""))
                    vecTem.add(linea);
            }
            Object[] arreglo = vecTem.toArray();
            Collator collator = Collator.getInstance(getLocaleArgentina());
            Arrays.sort(arreglo, collator);
            for (Object anArreglo : arreglo) {
                res += anArreglo + "\n";
            }
        }
        catch (FileNotFoundException  ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public static String ordenar(String pTxt, String pSeparador){
        String linea;
        String res = "";
        int hasta;
        int desde = 0;
        List<String> vecTem = new ArrayList<String>();
        Object[] arreglo;
        while((hasta = pTxt.indexOf(pSeparador, desde)) > 0){
            linea = pTxt.substring(desde, hasta);
            //System.out.println( linea);
            if (!linea.equals(""))
                vecTem.add(linea);
            desde = hasta + 1;
        }
        if (pTxt.length() > desde){
            linea = pTxt.substring(desde).trim();
            if (!linea.equals(""))
                vecTem.add(linea);
        }

        arreglo = vecTem.toArray();
        Collator collator = Collator.getInstance(getLocaleArgentina());
        Arrays.sort(arreglo, collator);

        for (Object anArreglo : arreglo) {
            res += anArreglo + "\n";
        }

        return res;
    }

     /**
     * Lee las propiedades del archivo y las asigna a System.properties
     */
    public static void loadProperties(String pFilename){
        try
        {
            Properties system = System.getProperties();
            File f = new File(pFilename);
            FileInputStream fi = new FileInputStream(f);
            system.load(fi);
            fi.close();
        }catch(IOException e){
            logger.error(e.getMessage(), e);
        }
    }

    public static void pegarEnElPortapapeles(String txt){
        //Obtengo el clipboard
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        StringSelection contents = new StringSelection(txt);
        //Lo seteo en el clipboard
        cb.setContents(contents, null);
    }

    public static String copiarDesdeElPortapapeles() throws Exception{
        //Obtengo el clipboard
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        //Lo seteo en el clipboard
        Transferable t = cb.getContents(null);
        Object o = t.getTransferData(DataFlavor.stringFlavor);
        if (o == null)
            return "";
        else
            return o.toString();
    }

    /**
     * Ejemplo de cómo se compila
     * @param sourcePath
     * @param source
     * @return
     */
	 //poner en el path el jar tools.jar (j2sdk1.4.2_03\lib)
    public int compilar(String sourcePath, String source){
//        String[] params = {};
        throw new RuntimeException("Falta agregar librería");
//        return com.sun.tools.javac.Main.compile(params);
        /*
        String[] params = {
//            "-sourcepath", sp.getAbsolutePath(),
            "-d", f.getParent(),//_dirTemp, //dir salida
            f.getAbsolutePath()
        };
        return com.sun.tools.javac.Main.compile(params);
            */
    }
    /**
     * Ejemplo:
     * bytes[0] = 240 (F0), bytes[1] = 30 (1E)
     *     devuelve
     * bitset = [1111 0000  0001 1110]
     *
     * @param bytes
     * @return
     */
    public static BitSet unsignedByte2Bits(int[] bytes){
        BitSet res = new BitSet();
        int ind = 0;
        for (int x : bytes) {
            if (x > 127) {
                res.set(ind);
                x -= 128;
            }

            ind++;          //pos 1
            if (x > 63) {
                res.set(ind);
                x -= 64;
            }

            ind++;          //pos 2
            if (x > 31) {
                res.set(ind);
                x -= 32;
            }

            ind++;          //pos 3
            if (x > 15) {
                res.set(ind);
                x -= 16;
            }

            ind++;          //pos 4
            if (x > 7) {
                res.set(ind);
                x -= 8;
            }

            ind++;          //pos 5
            if (x > 3) {
                res.set(ind);
                x -= 4;
            }

            ind++;          //pos 6
            if (x > 1) {
                res.set(ind);
                x -= 2;
            }

            ind++;          //pos 7
            if (x > 0) {
                res.set(ind);
            }

            ind++; //pos 0
        }
        return res;
    }

    public static <T> List<T> getList(Set<T> set){
        List<T> res = new ArrayList<T>(set.size());
        res.addAll(0, set);
        return res;
    }

    public static void main(String[] args) throws Exception{
//        copiarDir("F:\\Java\\ProyectosJava\\Pablo\\bak", "C:\\temp");
//        System.out.println("listo");

    }

    private  static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }
}