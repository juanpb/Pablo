package p.aplic.verfotos.v3;

//import org.junit.Test;
//import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import p.aplic.verfotos.ModoVisualizacion;
import p.util.UtilFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Jun 19, 2008
 * Time: 3:14:40 PM
 */
public class Config {
    private ModoVisualizacion modoVisualizacion = ModoVisualizacion.TAMANO_REAL_A_PANTALLA;
    private List<String> directorios = null;
    private List<String> filtroFotos = null;
    private List<String> extensiones = null;
    private long tiempoPorFoto = 4000;
    private long framesPorSegundo = 60;
    private boolean repetir = true;
    private boolean recursivo = true;
    private boolean agrandarParaLlenarPantalla = false;


    private Logger log = Logger.getLogger(Config.class);

    public Config()  {

    }
    public Config(String archivoConfig) throws IOException {
        cargar(archivoConfig);
    }

    public void setArchivoConfig(String arch) throws IOException {
        cargar(arch);
    }

    private void cargar(String arch) throws IOException {
        List<String> lst = UtilFile.getArchivoPorLinea(arch);
        for (String s : lst) {
            s = s.toLowerCase();
            if (s.startsWith("tiempo_por_foto"))
                tiempoPorFoto = getLong(s);
            else if (s.startsWith("fotos_por_segundo"))
                framesPorSegundo = getLong(s);
            else if (s.startsWith("repetir"))
                repetir = getBoolean(s);
            else if (s.startsWith("recursivo"))
                recursivo = getBoolean(s);
            else if (s.startsWith("agrandar_para_llenar_pantalla"))
                recursivo = getBoolean(s);
            else if (s.startsWith("directorios"))
                directorios = getList(s, ";");
            else if (s.startsWith("extensiones"))
                extensiones = getList(s, ";");
            else if (s.startsWith("filtroFotos"))
                filtroFotos = getList(s, ";");
            else if (s.startsWith("modo_visualizacion"))
                modoVisualizacion = ModoVisualizacion.parse(getValue(s));
        }
        log.debug("Config cargado con:");
        log.debug("     tiempoPorFoto: " + tiempoPorFoto);
        log.debug("     framesPorSegundo: " + framesPorSegundo);
        log.debug("     repetir: " + repetir);
        log.debug("     recursivo: " + recursivo);
        log.debug("     agrandar_para_llenar_pantalla: " + agrandarParaLlenarPantalla);
        log.debug("     directorios: " + directorios);
        log.debug("     extensiones: " + extensiones);
        log.debug("     modo_visualizacion: " + modoVisualizacion);
        log.debug("     filtroFotos: " + filtroFotos);
    }

    private List<String> getList(String src, String separador){
        src = getValue(src);
        StringTokenizer st = new StringTokenizer(src, separador, false);
        List<String> res = new ArrayList<String>();
        while (st.hasMoreElements()) {
            res.add(st.nextToken());
        }
        return res;
    }

//    public static void main(String[] args) {
////        Config c = new Config();
//        System.out.println("");
//    }

    private Boolean getBoolean(String s) {
        String v = getValue(s).toLowerCase();
        return v.startsWith("s") || v.startsWith("y") || v.startsWith("v");
    }

    /**
     * Devuelve la parte derecha del igual
     * ejemplo: 'clave  = valor ' => 'valor'
     */
    private Long getLong(String s) {
        String s2 = getValue(s);
        return Long.parseLong(s2);
    }

    private String getValue(String s) {
        int i = s.indexOf("=");
        return s.substring(i+1).trim();
    }

//    @Test
//    public void testGetLong(){
//        String x = "asdf = 75";
//        assertEquals(new Long(75), getLong(x));
//        assertNotSame("asd", 76, getLong(x));
//    }


    public ModoVisualizacion getModoVisualizacion() {
        return modoVisualizacion;
    }

    public List<String> getDirectorios() {
        return directorios;
    }

    public List<String> getFiltroFotos() {
        return filtroFotos;
    }

    public long getTiempoPorFoto() {
        return tiempoPorFoto;
    }

    public long getFramesPorSegundo() {
        return framesPorSegundo;
    }

    public boolean isRepetir() {
        return repetir;
    }

    public boolean isRecursivo() {
        return recursivo;
    }

    public List<String> getExtensiones() {
        return extensiones;
    }

    public boolean isAgrandarParaLlenarPantalla() {
        return agrandarParaLlenarPantalla;
    }
}