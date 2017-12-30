package p.aplic.renombrarFotos;

import org.apache.log4j.Logger;
import p.fotos.Exif;
import p.util.Fechas;
import p.util.Util;
import p.util.UtilFile;
import p.util.UtilString;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Renombrar{
    private static final Logger logger = Logger.getLogger(Renombrar.class);
    public static final String SEPARADOR = "_";

    private RenombrarGUI gui = null;
    private int contadorFotosDesde = 1;

    public Renombrar(String[] args) {
        try {
            String dir = null;
            if (args.length > 0){
                int i=0;
                String arg0 = args[i];
                if (arg0.startsWith("-d")){
                    dir = arg0.substring(2);
                    if (!new File(dir).exists())
                        dir = null;
                    i++;
                }

                if (args.length > i){
                    String pathArchIni = args[i];
                    Util.loadProperties(pathArchIni);
                }
            }
            gui = new RenombrarGUI(this);
            if (dir != null)
                gui.setDirectorio(dir);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setDirectorio(String d) {
        gui.setDirectorio(d);
    }
    public static void main(String[] args) {
        new Renombrar(args);
    }



    public void grabar() throws Exception {
        FileOutputStream fos = null;
        StringBuilder sb = new StringBuilder();
        try {
            String path;
            List<Thumb> thumbs = gui.getThumbs();
            int cantFotos = thumbs.size();
            int cont = 1;
            for (Thumb thumb : thumbs) {
                String tit = "[ " + cont++ + "/" + cantFotos + " ] - " +
                        thumb.getArchivo().getName();
                gui.setTitle(tit);
                //renombro el archivo/directorio
                if (thumb.cambio()){
                    File file = thumb.getArchivo();
                    path = file.getParent();

                    String nuevoNombre = path + "\\" + thumb.getNuevoNombre();

                    File nuevo = new File(nuevoNombre);
                    boolean b = file.renameTo(nuevo);
                    if (!b){
                        JOptionPane.showMessageDialog(null, "No se pudo grabar el archivo: " + nuevoNombre);
                    }else{
                        thumb.setArchivo(nuevo);
                    }


                    //deshacer
                    if (!file.getAbsolutePath().startsWith("Z:\\temp")){
                        String viejo = "viejo: " + file.getAbsolutePath();
                        String x = "nuevo: " + nuevo.getAbsolutePath();
                        if (!b)
                            x += "\n" + "^^^^^FALLÓ^^^^^^^";
                        sb.append(viejo + "\n" + x + "\n" + "\n") ;
                    }
                }
            }
            UtilFile.logEnUserDir("desRen.txt", sb);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        finally{
            gui.setTitle("");
        }
    }

    /**
     * Renombra el nombre de los archivos con un contador
     * @param str
     */
    public void renombrarFoto(String fecha, String str){
//        if (str == null || fecha == null)
//            return;
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        int cont = 1;
//        String ext = "";
//
//        //Cuento cuántos nombres se van a modificar para saber cuántos '0' tengo
//        //que agregar antes del contador
//        int cantAModificar = 0;
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (editar(i, fila)) {
//                cantAModificar++;
//            }
//        }
//        int cantDigitos = Integer.toString(cantAModificar).length();
//
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//
//            if (!fila.esDir()){
//                String name = fila.getFile().getName();
//                int p = name.lastIndexOf(".");
//                if (p > 0){
//                    ext = name.substring(p);
//                }
//            }
//            String ceros = "";
//            int cantDigitos2 = Integer.toString(cont).length();
//            while (cantDigitos > cantDigitos2)
//            {
//                ceros += "0";
//                cantDigitos2++;
//            }
//
//            String str2 = str;
//            String fecha2 = fecha;
//
//            if (!str2.equals(""))
//                str2 = SEPARADOR + str2;
//            if (!fecha2.equals(""))
//                fecha2 += SEPARADOR;
//            String nuevo = fecha2 + ceros + cont + str2 + ext;
//            cont++;
//            fila.setFileName(nuevo);
//        }
//        tm.fireTableDataChanged();
    }

    public void renombrar(String viejo, String nuevo){
//        if (viejo == null || nuevo == null)
//            return;
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            String nStr = UtilString.reemplazarTodo(str, viejo, nuevo);
//            fila.setFileName(nStr);
//        }
//        tm.fireTableDataChanged();
    }

    private boolean editar(Thumb t) {
        return true;
    }

    public void mayusculaMinuscula(){
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            str = mayMin(str);
//            fila.setFileName(str);
//        }
//        tm.fireTableDataChanged();
    }

    public void insertarDespuesDe(String nuevo, String cond){
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            fila.setFileName(
//                    UtilString.insertarDespuesDe(str, nuevo, cond));
//        }
//        tm.fireTableDataChanged();
    }

    public void insertarAntesDe(String nuevo, String cond){
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            fila.setFileName(
//                    UtilString.insertarAntesDe(str, nuevo, cond));
//        }
//        tm.fireTableDataChanged();
    }

    public void insertarAlPrincipio(String nuevo){
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = nuevo + fila.getFileName();
//            fila.setFileName(str);
//        }
//        tm.fireTableDataChanged();
    }

    public void insertarAlFinal(String nuevo){
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            boolean adle = !fila.esDir();
//            fila.setFileName(UtilString.insertarAlFinal(str, nuevo, adle));
//        }
//        tm.fireTableDataChanged();
    }
    /**
     * Si no encuentra 'hasta' no hace nada
     */
    public void eliminarDesdeLaIzquierda(String hasta){
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            fila.setFileName(UtilString.eliminarDesdeLaIzquierda(str, hasta));
//        }
//        tm.fireTableDataChanged();
    }

    /**
     * Respeta la extensión
     */
    public void eliminarDesdeLaDerecha(String hasta){
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            fila.setFileName(UtilString.eliminarDesdeLaDerecha(str, hasta));
//        }
//        tm.fireTableDataChanged();
    }


    public void numerar() {
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        int cont = 1;
//
//        //Cuenta cuántas nombres se van a modificar para saber cuántos '0' tengo
//        //que agregar antes del contador
//        int cantAModificar = 0;
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila =  dt.get(i);
//            if (editar(i, fila)) {
//                cantAModificar++;
//            }
//        }
//
//        int cantDigitos = Integer.toString(cantAModificar).length();
//
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//
//            String ceros = "";
//            int cantDigitos2 = Integer.toString(cont).length();
//            while (cantDigitos > cantDigitos2)
//            {
//                ceros += "0";
//                cantDigitos2++;
//            }
//            String str = fila.getFileName();
//            String nuevo = ceros + cont + SEPARADOR + str;
//            cont++;
//            fila.setFileName(nuevo);
//        }
//        tm.fireTableDataChanged();
    }

    public void setContadorDeFotosDesde(int c){
        contadorFotosDesde = c;
    }


    public void insertarExif() {
        List<Thumb> thumbs = gui.getThumbs();
        for (Thumb thumb : thumbs) {
            if (!editar(thumb))
                continue;
            File archivo = thumb.getArchivo();
            Exif ex = new Exif(archivo);
            String n = archivo.getName();

            String e = UtilString.reemplazarTodo(ex.toString(), "/", "-");
            int h = n.lastIndexOf(".");
            String nse = n.substring(0, h);
            String ext = n.substring(h);

            String nn = nse + "[ " + e + " ]" + ext;
            thumb.setNuevoNombre(nn);
        }
    }

/*****************************************************************************/
/*                        Métodos privados                                   */
/*****************************************************************************/
    private boolean esFoto(String fileName) {
    return fileName.toLowerCase().endsWith("jpg")
            || fileName.toLowerCase().endsWith("jpeg");
}

    /**
     * Devuelve el String en minúscula salvo el comienzo de cada palabra
     */
    public static String mayMin(String str){
        String res = UtilString.mayusMinus(str);
        res = UtilString.reemplazarTodo(res, ".Mp3", ".mp3");
        res = UtilString.reemplazarTodo(res, " .mp3", ".mp3");
        res = UtilString.reemplazarTodo(res, "Ii", "II");
        res = UtilString.reemplazarTodo(res, "Iii", "III");
        res = UtilString.reemplazarTodo(res, "IIi", "III");

        return res;
    }

    public void eliminarCantDesdeLaDerecha(String text) {
//        int cant;
//        try{
//            cant = Integer.parseInt(text);
//        }
//        catch(Exception e){
//            return ;
//        }
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            fila.setFileName(UtilString.eliminarCantDesdeLaDerecha(str, cant));
//        }
//        tm.fireTableDataChanged();
    }

    public void eliminarCantDesdeLaIzquierda(String text) {
//        int cant;
//        try{
//            cant = Integer.parseInt(text);
//        }
//        catch(Exception e){
//            return ;
//        }
//        FotosTableModel tm = gui.getTableModel();
//        List<Thumb> dt = gui.getDatosTabla();
//        for (int i = 1; i < tm.getRowCount(); i++) {
//            Thumb fila = dt.get(i);
//            if (!editar(i, fila))
//                continue;
//            String str = fila.getFileName();
//            fila.setFileName(UtilString.eliminarCantDesdeLaIzquierda(str, cant));
//        }
//        tm.fireTableDataChanged();
    }

    public void insertarFecha() {
        List<Thumb> thumbs = gui.getThumbs();
        String fecha = "";

        int cont = 0;
        Map<String, Integer> contadorFechas = new HashMap<String, Integer>();

        for (Thumb thumb : thumbs) {
            if (!editar(thumb))
                continue;

            Exif exif = new Exif(thumb.getArchivo());

            long lm = exif.getFechaOriginal().getTime();
            String amd = Fechas.getAAAAMMDD("-", lm);
            String nuevoNom;
            if (!fecha.equals(amd)){
                if (cont != 0){//guardo el contador
                    contadorFechas.put(fecha, cont);
                }
                Integer contViejo = contadorFechas.get(amd);
                if (contViejo != null)
                    cont = contViejo;
                else
                    cont = contadorFotosDesde -1;
                fecha = amd;
            }
            cont++;
            String sep = "_";
            if (cont < 10)
                sep += "0";
            nuevoNom = amd + sep + cont + ".jpg";
            thumb.setNuevoNombre(nuevoNom);
        }
    }

}






