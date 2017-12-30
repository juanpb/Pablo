package p.aplic.peliculas;

import p.aplic.peliculas.util.Log;
import p.aplic.peliculas.util.Util;

import javax.swing.*;

/**
 * User: Administrador
 * Date: 03/02/2007
 * Time: 00:45:08
 */
public class Main {

    public static void main(String[] args) {
        new Main(args);
    }

    private Main(String[] args) {

        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];

        p.util.Util.loadProperties(configPath);
        String xmlPath = System.getProperty("xmlPath");


        Peliculas pelis;
        try {
            pelis = Util.getPeliculas(xmlPath);
        } catch (Exception e) {
            Log.error(e);
            return ;
        }

        PeliculasFrame pp = new PeliculasFrame(pelis);
        pp.pack();
        pp.setVisible(true);
    }

}
