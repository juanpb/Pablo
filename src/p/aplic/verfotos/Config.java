package p.aplic.verfotos;

//import org.junit.Test;
//import static org.junit.Assert.*;

import org.apache.log4j.Logger;
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
    private List<String> directoriosSinFiltrar = null;
    private List<String> extensiones = null;
    private long tiempoPorFoto = 4000;
    private int numeroDeMonitor = 0;
    private long framesPorSegundo = 60;
    private List<String> filtroFotos = null;
    private boolean tipoFiltroOr = true;
    private boolean repetir = true;
    private boolean recursivo = true;
    private boolean bloquearPc = false;
    private String teclaDesbloqueo = null;
    private boolean usarAntibloqueo = false;
    private boolean mostrarMezclado = false;
    private boolean arrancarSlideShow = false;
    private boolean arrancarSlideShowDeUna = false;
    private boolean agrandarParaLlenarPantalla = false;
    private boolean posicionDeInicioAleatoria = false;


    private Logger log = Logger.getLogger(Config.class);
    private static final String SEPARADOR = ";";

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
            //si el nombre de una propiedad es prefijo de otra => ponerla después

            if (s.startsWith("tiempo_por_foto"))
                tiempoPorFoto = getLong(s);
            else if (s.startsWith("frames_por_segundo"))
                framesPorSegundo = getLong(s);
            else if (s.startsWith("repetir"))
                repetir = getBoolean(s);
            else if (s.startsWith("recursivo"))
                recursivo = getBoolean(s);
            else if (s.startsWith("bloquear_pc"))
                bloquearPc = getBoolean(s);
            else if (s.startsWith("usar_antibloqueo"))
                usarAntibloqueo = getBoolean(s);
            else if (s.startsWith("posicion_de_inicio_aleatoria"))
                posicionDeInicioAleatoria = getBoolean(s);
            else if (s.startsWith("arrancar_slide_show_de_una"))
                arrancarSlideShowDeUna = getBoolean(s);
            else if (s.startsWith("arrancar_slide_show"))
                arrancarSlideShow = getBoolean(s);
            else if (s.startsWith("tipoFiltroOr"))
                tipoFiltroOr = getBoolean(s);
            else if (s.startsWith("mostrar_mezclado"))
                mostrarMezclado = getBoolean(s);
            else if (s.startsWith("agrandar_para_llenar_pantalla"))
                agrandarParaLlenarPantalla = getBoolean(s);
            else if (s.startsWith("directoriosSinFiltrar"))
                directoriosSinFiltrar = getList(s, SEPARADOR);
            else if (s.startsWith("directorios"))
                directorios = getList(s, SEPARADOR);
            else if (s.startsWith("filtroFotos"))
                filtroFotos = getList(s, SEPARADOR);
            else if (s.startsWith("extensiones"))
                extensiones = getList(s, SEPARADOR);
            else if (s.startsWith("modo_visualizacion"))
                modoVisualizacion = ModoVisualizacion.parse(getValue(s));
            else if (s.startsWith("numeroDeMonitor"))
                numeroDeMonitor = getInt(s);
            else if (s.startsWith("teclaDesbloqueo")){
                if (!s.trim().equals(""))
                    teclaDesbloqueo = getValue(s);
            }
        }
        log.info("Config cargado con:");
        log.info("     tiempoPorFoto: " + tiempoPorFoto);
        log.info("     framesPorSegundo: " + framesPorSegundo);
        log.info("     repetir: " + repetir);
        log.info("     recursivo: " + recursivo);
        log.info("     bloquearPc: " + bloquearPc);
        log.info("     agrandar_para_llenar_pantalla: " + agrandarParaLlenarPantalla);
        log.info("     directorios: " + directorios);
        log.info("     directoriosSinFiltrar: " + directoriosSinFiltrar);
        log.info("     extensiones: " + extensiones);
        log.info("     modo_visualizacion: " + modoVisualizacion);
        log.info("     mostrarMezclado: " + mostrarMezclado);
        log.info("     arrancarSlideShow: " + arrancarSlideShow);
        log.info("     numeroDeMonitor: " + numeroDeMonitor);
        log.info("     filtroFotos: " + filtroFotos);
        log.info("     tipoFiltroOr: " + tipoFiltroOr);
        log.info("     teclaDesbloqueo" + teclaDesbloqueo);
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

    public static void main(String[] args) throws IOException {
        while (true) {
            int read = System.in.read();
            if (read != 10)
                System.out.println(read);
        }
    }

    private Boolean getBoolean(String s) {
        String v = getValue(s).toLowerCase();
        return v.startsWith("s") || v.startsWith("y") || v.startsWith("t") || v.startsWith("v");
    }

    /**
     * Devuelve la parte derecha del igual
     * ejemplo: 'clave  = valor ' => 'valor'
     */
    private Long getLong(String s) {
        String s2 = getValue(s);
        return Long.parseLong(s2);
    }

    private int getInt(String s) {
        String s2 = getValue(s);
        return Integer.parseInt(s2);
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

    public List<String> getDirectoriosSinFiltrar() {
        return directoriosSinFiltrar;
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

    public List<String> getFiltroFotos() {
        return filtroFotos;
    }

    public boolean isBloquearPc() {
        return bloquearPc;
    }

    public void setBloquearPc(boolean bloquearPc) {
        this.bloquearPc = bloquearPc;
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

    public void setExtensiones(String extensiones) {
        setExtensiones(getList(extensiones, SEPARADOR));
    }

    public void setDirectorios(String directorios) {
        setDirectorios(getList(directorios, SEPARADOR));
    }

    public void setDirectoriosSinFiltrar(String dsf) {
        setDirectoriosSinFiltrar(getList(dsf, SEPARADOR));
    }

    public void setFiltroFotos(String filtroFotos) {
        setFiltroFotos(getList(filtroFotos, SEPARADOR));
    }

    public void setDirectorios(List<String> directorios) {
        this.directorios = directorios;
    }

    public void setDirectoriosSinFiltrar(List<String> dsf) {
        this.directoriosSinFiltrar = dsf;
    }

    public void setExtensiones(List<String> extensiones) {
        this.extensiones = extensiones;
    }

    public void setFiltroFotos(List<String> filtroFotos) {
        this.filtroFotos = filtroFotos;
    }

    public void setTiempoPorFoto(String t) {
        this.tiempoPorFoto = Long.parseLong(t);
    }
    public void setRepetir(boolean repetir) {
        this.repetir = repetir;
    }

    public void setRecursivo(boolean recursivo) {
        this.recursivo = recursivo;
    }

    public boolean isPosicionDeInicioAleatoria() {
        return posicionDeInicioAleatoria;
    }

    public void setPosicionDeInicioAleatoria(boolean posicionDeInicioAleatoria) {
        this.posicionDeInicioAleatoria = posicionDeInicioAleatoria;
    }

    public boolean isUsarAntibloqueo() {
        return usarAntibloqueo;
    }

    public void setUsarAntibloqueo(boolean usarAntibloqueo) {
        this.usarAntibloqueo = usarAntibloqueo;
    }

    public boolean isMostrarMezclado() {
        return mostrarMezclado;
    }

    public void setMostrarMezclado(boolean mostrarMezclado) {
        this.mostrarMezclado = mostrarMezclado;
    }

    public boolean isArrancarSlideShow() {
        return arrancarSlideShow;
    }

    public void setArrancarSlideShow(boolean arrancarSlideShow) {
        this.arrancarSlideShow = arrancarSlideShow;
    }

    public boolean isArrancarSlideShowDeUna() {
        return arrancarSlideShowDeUna;
    }

    public void setArrancarSlideShowDeUna(boolean arrancarSlideShowDeUna) {
        this.arrancarSlideShowDeUna = arrancarSlideShowDeUna;
    }

    public int getNumeroDeMonitor() {
        return numeroDeMonitor;
    }

    public boolean isTipoFiltroOr() {
        return tipoFiltroOr;
    }

    public void setTipoFiltroOr(boolean tipoFiltroOr) {
        this.tipoFiltroOr = tipoFiltroOr;
    }

    public Character getTeclaDesbloqueo() {
        if (teclaDesbloqueo == null)
            return null;
        else
            return teclaDesbloqueo.charAt(0);
    }
}
