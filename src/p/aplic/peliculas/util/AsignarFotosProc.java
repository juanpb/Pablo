package p.aplic.peliculas.util;

import org.jdom.JDOMException;
import p.aplic.peliculas.Pelicula;
import p.aplic.peliculas.Peliculas;
import p.aplic.peliculas.Tag;
import p.util.Constantes;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: Administrador
 * Date: 25/03/2007
 * Time: 19:16:45
 */
public class AsignarFotosProc {
    String fotosPath = "F:\\_videos_\\_info_\\fotos\\";
    public static void main(String[] args) throws IOException, JDOMException {
        args = new String[1];
        args[0] = "F:\\Java\\ProyectosJava\\Pablo\\src\\p\\aplic\\peliculas\\config.ini";
        new AsignarFotosProc(args);
    }

    private AsignarFotosProc(String[] args) throws IOException, JDOMException {
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
        for (Pelicula p : pelis.getPeliculas()) {
            List<String> posters = p.getPosters();
            if (posters.size() == 0){
                String nombre = p.getNombre() + ".jpg";
                nombre = parchear(nombre);
                File f = new File(fotosPath, nombre);
                if (!f.exists()){
                    boolean preguntar = true;
                    while (preguntar){
                        String msg = "'Cancelar' -> ignora " + Constantes.NUEVA_LINEA +
                                "vacío + 'Aceptar' para reintentar" +
                                  Constantes.NUEVA_LINEA +
                                "o nueva dir + 'Aceptar'";
                        nombre = JOptionPane.showInputDialog(msg, nombre);
                        if (nombre == null) //se apretó cancelar
                            preguntar = false;
                        else if (!nombre.trim().equals("")){
                            //si se ingresó algo pruebo con la nueva dir
                            f = new File (fotosPath, nombre);
                        }
                        //else si se dejó en blanco vuelvo a probar con la dir
                        //original
                        preguntar = preguntar && !f.exists();
                    }
                }

                if (nombre != null){
                    posters.add(nombre);

                    f = new File(fotosPath, nombre + "_2.jpg");
                    if (f.exists()){
                        posters.add(nombre + "_2.jpg");

                        f = new File(fotosPath, nombre + "_3.jpg");
                        if (f.exists()){
                            posters.add(nombre + "_3.jpg");
                        }
                    }
                }
            }
        }
        Util.grabar(xmlPath, pelis);
    }

    public static String parchear(String nombre) {
        nombre = nombre.replaceAll("á", "a");
        nombre = nombre.replaceAll("é", "e");
        nombre = nombre.replaceAll("í", "i");
        nombre = nombre.replaceAll("ó", "o");
        nombre = nombre.replaceAll("ú", "u");
        nombre = nombre.replaceAll(":", ",");
        nombre = nombre.replaceAll("ñ", "n");
        nombre = nombre.replaceAll("'", "");
        nombre = nombre.replaceAll("à", "a");

        return nombre;
    }
}
