package p.proc.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import p.util.UtilFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Prueba {
    static private List<AsigSevHs> asigSevHses = new ArrayList<AsigSevHs>();
    static private List<String> severidades = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
//        String file = "D:\\P\\java\\codigo\\Pablo\\config\\hs_qc.txt";
//        File fileOut = new File("D:\\P\\java\\codigo\\Pablo\\salidaSLAS.txt");
        if (args.length !=2 ){
            System.out.println("Modo de uso: p.proc.velocity.Prueba ARCHIVO_SLAS ARCHIVO_SALIDA");
            System.exit(-1);
        }
        String file = args[0];
        File fileOut = new File(args[1]);
        cargar(file);
        generarSalida(fileOut);
        System.out.println("Se generó el archivo de salida: "+ fileOut.getAbsolutePath());
    }

    /*
    public static List<String> getGrupos() {
        List<String> res = new ArrayList<String>();
        for (AsigSevHs asigSevHse : asigSevHses) {
            res.add(asigSevHse.asignadoA);
        }
        return res;
    }    */

    private static void cargar(String f) throws IOException {
        List<String> archivoPorLinea = UtilFile.getArchivoPorLinea(f);

        cargarSeveridades(archivoPorLinea.get(0));
        archivoPorLinea.remove(0);//borro las severidades

        for (String s : archivoPorLinea) {
            asigSevHses.add(new AsigSevHs(s));
        }
    }

    private static void cargarSeveridades(String linea) {
        StringTokenizer st = new StringTokenizer(linea, ",", false);
        st.nextToken();//el primero es dummy
        while (st.hasMoreTokens()) {
            severidades.add(st.nextToken().trim());
        }
    }


    public static void generarSalida(File fileOut){
        PrintWriter writer = null;
        VelocityContext context = new VelocityContext();
        context.put("severidades", severidades);
        context.put("slas", asigSevHses);

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template template = ve.getTemplate( "templates/ej.vm" )  ; //poner el template con los binarios, no funca con el path completo

        try{
            writer = new PrintWriter (fileOut);
            template.merge( context , writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if ( writer != null){
                try{
                    writer.flush();
                    writer.close();
                }catch( Exception ee ) {
                    ee.printStackTrace();
                }
            }
        }
    }


}
