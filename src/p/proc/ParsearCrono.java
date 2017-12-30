package p.proc;

import org.apache.log4j.Logger;
import p.util.Constantes;
import p.util.UtilFile;
import p.util.UtilString;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class ParsearCrono implements Constantes{
    private static final Logger logger = Logger.getLogger(ParsearCrono.class);

    public static void main(String[] args){
        String x = "C:\\Borra\\cr2.txt";
        new ParsearCrono(x);
    }

    public ParsearCrono(String x) {
        try
        {
            List lista = UtilFile.getArchivoPorLinea(new File(x));
            List listaElem = new Vector();
            System.out.println("1");

            System.out.println("lista.size() = " + lista.size());
            for (int i = 0; i <lista.size(); i++){
                String l = (String)lista.get(i);
                Elem el = parsear(l);
                if (el != null)
                    listaElem.add(el);
            }
            Object[] os = listaElem.toArray();
            Arrays.sort(os, new ElCompa());

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i <os.length; i++){
                Elem el = (Elem)os[i];
                if (el != null){
                    String f = el.fecha;
                    String l = el.lugar;
                    String t = el.txt;
                    sb.append(f).append(TAB_CHARACTER);
                    sb.append(l).append(TAB_CHARACTER);
                    sb.append(t).append(NUEVA_LINEA);
                }
            }
            System.out.println(sb.toString());
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }

    }

    private Elem parsear(String l) {
        Elem res = null;
        try{
            if (l == null || l.trim().equals(""))
                return res;
            else{
                res = new Elem();
                int ind = l.indexOf("\t");
                res.fecha = l.substring(0, ind);
                l = l.substring(ind).trim();

                ind = l.indexOf(TAB_CHARACTER);
                res.lugar = l.substring(0, ind);
                l = l.substring(ind).trim();

                res.txt = l;
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }

        return res;
    }
}

class Elem{
    String fecha = null;
    String lugar = null;
    String txt = null;


}

class ElCompa implements Comparator{
    private static final Logger logger = Logger.getLogger(ElCompa.class);
    public int compare(Object o1, Object o2)    {
        int res = 5;
        Elem e1 = null;
        Elem e2 = null;

        try{
            e1 = (Elem) o1;
            e2 = (Elem) o2;
            if (e1 == null || e1.fecha == null)
                res = 0;
            else if (e2 == null || e2.fecha == null)
                res = 0;
            else{
                String f1 = UtilString.reemplazarTodo(e1.fecha, "c. ", "").trim();
                String f2 = UtilString.reemplazarTodo(e2.fecha, "c. ", "").trim();

                //si hay dos fechas me quedo con la primera
                int i = f1.indexOf(" - ");
                if (i > 0)
                    f1 = f1.substring(0, i).trim();

                i = f2.indexOf(" - ");
                if (i > 0)
                    f2 = f2.substring(0, i).trim();

                //elimino los meses
                i = f1.indexOf(". ");
                while (i > 0){
                    f1 = f1.substring(i + 1).trim();
                    i = f1.indexOf(".");
                }

                i = f2.indexOf(".");
                while (i > 0){
                    f2 = f2.substring(i + 1).trim();
                    i = f2.indexOf(".");
                }
                int bc1 = f1.indexOf("BC");
                int bc2 = f2.indexOf("BC");
                if (bc1 > 0 && bc2 < 0)
                    res = -1; //el primero es menor que el segundo
                else if (bc1 < 0 && bc2 > 0)
                    res = 1;
                else if (bc1 > 0 && bc2 > 0){
                    f1 = UtilString.reemplazarTodo(f1, "BC", "").trim();
                    f2 = UtilString.reemplazarTodo(f2, "BC", "").trim();

                    int x1 = Integer.parseInt(f1);
                    int x2 = Integer.parseInt(f2);
                    if (x1 < x2)
                        res = 1;
                    else if (x1 > x2)
                        res = -1;
                    else
                        res = 0;
                }
                //ninguno es BC
                else{
                    int x1 = Integer.parseInt(f1);
                    int x2 = Integer.parseInt(f2);
                    if (x1 < x2)
                        res = -1;
                    else if (x1 > x2)
                        res = 1;
                    else
                        res = 0;
                }
            }
        }catch(RuntimeException e){
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("e1.fecha = " + e1.fecha);
            System.out.println("e2.fecha = " + e2.fecha);
            logger.error(e.getMessage(), e);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            throw e;
        }
        if (e1.fecha == null)
            e1.fecha = "";
        if (e2.fecha == null)
            e2.fecha = "";

//        if (res == 0){
//            System.out.println(e1.fecha + " = " + e2.fecha);
//        }
//        else if (res > 0){
//            System.out.println(e1.fecha + " posterior " + e2.fecha);
//        }
//        else
//            System.out.println(e1.fecha + " anterior " + e2.fecha);
        return res;
    }

    public boolean equals(Object obj){
        return false;
    }
}