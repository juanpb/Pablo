package p.aplic.tem2Excel;

import org.apache.log4j.Logger;
import p.util.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: Administrador
 * Date: 22/01/2006
 * Time: 13:13:41
 */
public class Control {
    static private Map cache = new HashMap();
    static private char separador = Constantes.TAB_CHARACTER;
    static private String separadorLina = Constantes.NUEVA_LINEA;
    private static String dirTem = "F:\\tem";
    private static List<File> m3u = new ArrayList<File>();
    private static final Logger logger = Logger.getLogger(Control.class);

    /**
     * Si es distinto de 1 se generan mal los m3u
     */
    private static final int CANT_CICLOS = 1;


    public static String getLista(String dir) throws Exception {
        StringBuffer sb = new StringBuffer();
        File raiz = new File(dir);
        if (!raiz.exists()){
            mostrarMsg("No existe el directorio " + raiz.getAbsolutePath());
            return null;
        }
        List dirs = getDirectoriosUnNivel(raiz);
        int ind = 0;
        boolean continuar = true;
        while (continuar){
            continuar = false;

            for (Object dir1 : dirs) {
                File f = (File) dir1;
                List dirs2 = getDirectoriosUnNivel(f);
                if (dirs2 == null || dirs2.size() <= ind)
                    continue;
                continuar = true;
                File d = (File) dirs2.get(ind);
                agregar(sb, f.getName(), d);
                agregarAM3U(d);
            }
            ind++;
            if (ind == CANT_CICLOS)
                continuar = false;
            sb.append(separadorLina);
        }
        return sb.toString();
    }


    /**
     *
     * @param d el álbum, adentro tiene los mp3 o disco 1, etc.
     */
    private static void agregarAM3U(File d) {
        DirFileFilter f = new DirFileFilter(false, "mp3");
        if (d.getParent().endsWith("\\Varios2")){
            return ;
        }
        File[] files = d.listFiles(f);
        if (files.length > 0)
            m3u.addAll(Arrays.asList(files));
        else{
            //Disco 1 y Disco 2 => se agregan
            DirFileFilter f2 = new DirFileFilter(true);
            File[] dirs = d.listFiles(f2);
            if (dirs.length == 2
                    && (dirs[0].getName().equals("Disco 1")
                        ||dirs[0].getName().equals("Disco 01"))){
                files = dirs[0].listFiles(f);
                m3u.addAll(Arrays.asList(files));
                files = dirs[1].listFiles(f);
                m3u.addAll(Arrays.asList(files));
            }
            //Si tiene directorios con Disco 1, Disco 2, etc
            else if (dirs.length > 0){
                boolean encontrado = false;
                for (int i = 0; i < 10; i++) { //   busco en los 10 primeros
                    String n = dirs[0].getName();
                    String z = "Disco " + (i+1);
                    String z2 = "Disco 0" + (i + 1);
                    if (n.startsWith(z) || n.startsWith(z2) ){
                        encontrado = true;
                        files = dirs[0].listFiles(f);
                        m3u.addAll(Arrays.asList(files));
                        break;
                    }
                }
                if (!encontrado){
                    for (int i = 0; i < 10; i++) { //   busco en los 10 siguientes
                        if (dirs.length > i){
                            String n = dirs[0].getName();
                            if (n.startsWith("Disco 1" + (i+1)) ){
                                encontrado = true;
                                files = dirs[i].listFiles(f);
                                m3u.addAll(Arrays.asList(files));
                                break;
                            }
                        }
                    }
                }
                if (!encontrado){
                    String x = "No se agregó nada de "+ d.getAbsolutePath();
                    System.out.println(x);
                }
            }
        }
    }

    static private void agregar(StringBuffer res, String padre, File file) {
        if (padre.equals("Varios2"))
            return ;
        double size = UtilFile.getSize(file);
        double mb = size /1024d / 1024d;
        String mbs = mb + "";
        mbs = mbs.replace('.', ',');
        int ind = mbs.indexOf(",");
        if (ind + 2 < mbs.length())
            mbs = mbs.substring(0, ind + 2);
        res.append(padre + separador + file.getName() + separador + mbs + separadorLina);
    }

    static public List getDirectoriosUnNivel(File f) throws Exception{
        List res = null;
        String path = f.getAbsolutePath();
        Object o = cache.get(path);
        if (o != null)
            return (List)o;

        if (!f.exists())
            throw new Exception("No existe el archivo " + path);


        DirFileFilter filtro = new DirFileFilter(true);
        File[]  subd = f.listFiles(filtro);
        if (subd.length > 0){
            Object[] or = Util.ordenar(subd, new FileComparator());
            res = java.util.Arrays.asList(or);
        }

        cache.put(path, res);
        return res;
    }
    static private void mostrarMsg(String s) {
        JOptionPane.showMessageDialog(null, s);

    }

    public static void main(String[] args) throws Exception {
        String lista = getLista(dirTem);
        Util.pergarEnElPortapapeles(lista);
        System.out.println("Pegado en el portapapeles");
        File f = guardarM3U();
        System.out.println("Creado el m3u: " + f.getAbsolutePath());
    }

    private static File guardarM3U() {
//        String n = Fechas.getAAAADDMMHHMMSS("_");
        String n = "a";
        File f = new File("F:\\tem\\Varios2\\" + n + ".m3u");
        try {
            List<String> m3  = new ArrayList<String>(m3u.size());
            for (Object aM3u : m3u) {
                File f2 = (File) aM3u;
                String s = f2.getAbsolutePath();
                m3.add(s);
            }

            UtilFile.guardartArchivoPorLinea(f, m3, true);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return f;
    }
}
