package p.aplic.peliculas.util;

import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.Tag;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: JPB
 * Date: 12/12/11
 * Time: 8:45 AM
 */
public class BuscarUniqueIdRepetidosProc {
    public BuscarUniqueIdRepetidosProc(String[] args) throws IOException, JDOMException {
        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }
        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");
        List<Tag> tags = Util.getTags(System.getProperty("tags"));

        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath, tags);
        } catch (Exception e) {
            Log.error(e);
            return ;
        }
        List<Integer> repet = new ArrayList<Integer>();
        Set<Integer> set = new HashSet<Integer>();
        for (Pelicula p : pelis.getPeliculas()) {
            Integer uniqueId = p.getUniqueId();
            if (set.contains(uniqueId)){
                repet.add(uniqueId);
            }
            else{
                set.add(uniqueId);
            }
        }
        if (repet.size() == 0)
            System.out.println("Sin repetidos");
        else{
            System.out.println("repet = " + repet);
        }

        //busco hueco
        for(int i = 1; i < 1312; i++) {
            if (!set.contains(i))
                System.out.println("hueco: " + i);
        }
    }

    public static void main(String[] args) throws IOException, JDOMException {
        args = new String[1];
        args[0] = "D:\\P\\java\\codigo\\Pablo\\config\\pelis\\config.ini";
        new BuscarUniqueIdRepetidosProc(args);
    }
}
