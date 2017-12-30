package p.proc.pplan;

import org.apache.log4j.Logger;
import p.gui.superGui.Aplicacion;
import p.gui.superGui.Param;
import p.util.Constantes;
import p.util.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: JPB
 * Date: 21/04/13
 * Time: 00:14
 */
public class GenerarRechazos implements Aplicacion {
    private static final Logger logger = Logger.getLogger(GenerarRechazos.class);

    private static final String NOMBRE = "Generar Rechazos";

    private static final int TAM_A_COMPARAR = 102;


    public String ejecutar(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();
        int cantRechazos;
        int cantRechazosNoEncontrados;
        try {

            File dirRechNuevo = new File (params.get(2));

            String n = "D:\\RootBuild\\_pplan\\paralelo\\v3\\rechazos\\nuev\\nombres.txt";
            final List<String> nombres = UtilFile.getArchivoPorLinea(n);
            Map<String, String> rechazos2 = new HashMap<String, String>();


            for (String name : nombres) {
                // name = 06.00018.20130703165828.PG
                name = name.trim();
                if (name.startsWith("06.") && name.endsWith(".PG")){
                    //guardo salida
                    guardarRechazosNuevos(new ArrayList<String>(), name, dirRechNuevo);
                }
            }
            long fin = System.currentTimeMillis();
            long ms = (fin - ini);

            res.append("Archivos generados en : " + dirRechNuevo.getAbsolutePath()).append(Constantes.NUEVA_LINEA);
            res.append("Demor� " + ms + " ms");

            return res.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            res.append("Error: ").append(e.getMessage());
            return res.toString();
        }
    }


    public static String ejecutar(String dirSalida, String archConNombres) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();
        int cantRechazos;
        int cantRechazosNoEncontrados;
        try {

            File dirRechNuevo = new File (dirSalida);

            final List<String> nombres = UtilFile.getArchivoPorLinea(archConNombres);
            Map<String, String> rechazos2 = new HashMap<String, String>();


            for (String name : nombres) {
                // name = 06.00018.20130703165828.PG
                name = name.trim();
                if (name.startsWith("06.") && name.endsWith(".PG")){
                    //guardo salida
                    guardarRechazosNuevos(new ArrayList<String>(), name, dirRechNuevo);
                }
            }
            long fin = System.currentTimeMillis();
            long ms = (fin - ini);

            res.append("Archivos generados en : " + dirRechNuevo.getAbsolutePath()).append(Constantes.NUEVA_LINEA);
            res.append("Demor� " + ms + " ms");

            return res.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            res.append("Error: ").append(e.getMessage());
            return res.toString();
        }
    }


    public String ejecutar2(List<String> params) {
        long ini = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();
        int cantRechazos;
        int cantRechazosNoEncontrados;
        try {

            File dirPP = new File (params.get(0));
            File dirRechOrig = new File (params.get(1));
            File dirRechNuevo = new File (params.get(2));

            final File[] filesRechOrig = dirRechOrig.listFiles();
            final File[] filesPP = dirPP.listFiles();
            Map<String, String> rechazos = new HashMap<String, String>();

            List<String> rechazosNoMapeados = new ArrayList<String>();

            //junto todos los rechazos
            for (File rechOr : filesRechOrig) {
                rechazos.putAll(getRechazos(rechOr));
            }
            cantRechazos = rechazos.size();

            for (File archPP : filesPP) {
                String name = archPP.getName();//06.00018.20130703165828.PG
                if (name.startsWith("06.") && name.endsWith(".PG")){
                    //busco los rechazos
                    List<String> rechXLoteNuevo = buscar(archPP, rechazos);

                    //seteo nuevo lote
                    String nroLote = name.substring(3, 8);
                    rechXLoteNuevo = Tranformar.cambiarNroLotePP(nroLote, rechXLoteNuevo);

                    //guardo salida
                    guardarRechazosNuevos(rechXLoteNuevo, name, dirRechNuevo);
                }
            }
            cantRechazosNoEncontrados = rechazos.size();

            long fin = System.currentTimeMillis();
            long ms = (fin - ini);

            res.append("Archivos generados en : " + dirRechNuevo.getAbsolutePath()).append(Constantes.NUEVA_LINEA);
            res.append("cant. de rechazos: " + cantRechazos).append(Constantes.NUEVA_LINEA);
            res.append("cant. de rechazos no encontrados: " + cantRechazosNoEncontrados).append(Constantes.NUEVA_LINEA);
            final Collection<String> noEncontrados = rechazos.values();
            if (noEncontrados.size() > 0){
                System.out.println("Rechazos no encontrados: " + noEncontrados.size());

                for (String ne: noEncontrados) {
                    System.out.println(ne);
                }
            }

            res.append("Demor� " + ms + " ms");

            return res.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            res.append("Error: ").append(e.getMessage());
            return res.toString();
        }
    }

    private static void guardarRechazosNuevos(List<String> rechXLoteNuevo, String loteOriginal, File dirRechNuevo) throws IOException {
        //nuevo nombre de archivo
        //06.41700.20130511160338.PG -> 06.41700.ERROR.20130511160338.proc
        String nuevoNroLote = loteOriginal.substring(3, 8);
        String fechaGen = loteOriginal.substring(9, 23);
        String nuevoLote = loteOriginal.substring(0, 9) + "ERROR." + loteOriginal.substring(9, 23) + ".proc";
        File f = new File(dirRechNuevo, nuevoLote);
        if (f.exists())
            f = new File(f.getAbsolutePath() + System.currentTimeMillis());

        String h = RegPP.getHeader("06", "ERROR", fechaGen);
        String fo = RegPP.getFooter("06", "ERROR", fechaGen, rechXLoteNuevo);

        List<String> salida = new ArrayList<String>();
        salida.add(h);
        salida.addAll(rechXLoteNuevo);
        salida.add(fo);
        UtilFile.guardartArchivoPorLinea(f, salida);

    }

    private List<String> buscar(File archPP, Map<String, String> rechazos) throws IOException {
        //132
        List<String> res = new ArrayList<String>();
        final List<String> pps = RegPP.sacarHeaderYFooter(archPP);
        for (String p : pps) {
            final String k = p.substring(0, TAM_A_COMPARAR);
            String v = rechazos.get(k);
            if (v != null){
                res.add(v);
                rechazos.remove(k);
            }
        }
        return res;
    }

    //devuelve los rechazos (sin header ni footer)
    //devuelve una tupla con clave = los primeros TAM_A_COMPARAR caracteres, valor = l�nea completa
    private Map<String,String> getRechazos(File entrada) throws IOException {
        final List<String> strings = RegPP.sacarHeaderYFooter(entrada);
        final Map<String,String> res = new HashMap<String,String>();
        for (String s : strings) {
            res.put(s.substring(0, TAM_A_COMPARAR), s);
        }
        return res;
    }

    @Override
    public String getName() {
        return NOMBRE;
    }

    @Override
    public List<Param> getParams() {
        List<Param> res = new ArrayList<Param>();

        final Param p1 = new Param("Directorio PP");
        res.add(p1);
        final Param p2 = new Param("Dir. rech orig");
        res.add(p2);
        final Param p3 = new Param("Dir. rech nuevos");
        res.add(p3);

        return res;
    }

    public static void main1(String[] args) {
        String dirPP = "";
        String dirRechOrig = "";
        String dirRechNuevo = "D:\\RootBuild\\_pplan\\paralelo\\v3\\rechazos\\salida";

        List<String> ps = new ArrayList<String>();
        ps.add(dirPP);
        ps.add(dirRechOrig);
        ps.add(dirRechNuevo);

        final GenerarRechazos gr = new GenerarRechazos();
        final String res = gr.ejecutar(ps);
        System.out.println(res);

    }
    public static void main(String[] args) {
        if (args.length != 2){
            System.out.println("se debe pasar como parámetro directorio de salida y path del archivo con los nombres de los archivos 06");
            System.out.println("Ejemplo: java -classpath jpb.jar p.proc.pplan.GenerarRechazos D:\\temp\\salida D:\\temp\\nombres.txt");
            System.exit(-1);
        }
        String dirRechNuevo = args[0];
        String archNombres = args[1];

        final String res = GenerarRechazos.ejecutar(dirRechNuevo, archNombres);
        System.out.println(res);

    }
}
