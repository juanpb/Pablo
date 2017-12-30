package p.aplic.peliculas.util;

import org.jdom.JDOMException;

import java.io.IOException;

/**
 * User: Administrador
 * Date: 11/02/2007
 * Time: 15:50:59
 */
public class Xml2Html {
    private static String dir =
//            "D:\\P\\java\\codigo\\Pablo\\src\\p\\aplic\\peliculas\\";
            "C:\\Documents and Settings\\Administrador\\Escritorio\\Películas\\";
//            "F:\\Java\\ProyectosJava\\Pablo\\src\\p\\aplic\\peliculas\\";
    private static final String archSalida = dir + "vistos.html";
    private static final String archEntrada = dir + "vistos.xml";

    public static void main(String[] args) throws IOException, JDOMException {
//        if (args.length == 0){
//            String m = "Pasar como parámetro el archivo de config.";
//            JOptionPane.showMessageDialog(null, m);
//            return ;
//        }
//        p.util.Util.loadProperties(args[0]);
//
//        new Xml2Html().generarHTML(archEntrada, archSalida);
    }



//    public void generarHTML(String archEntrada, String archSalida)
//            throws IOException, JDOMException {
//        //todo: agregar a la otra versión
//        Peliculas peliculas = Util.getPeliculas(archEntrada);
//        HTML.generarHTML(peliculas, archSalida);
//    }


}