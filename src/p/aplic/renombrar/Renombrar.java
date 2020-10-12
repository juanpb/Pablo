package p.aplic.renombrar;

import de.ueberdosis.mp3info.UndersizedException;
import org.apache.log4j.Logger;
import p.fotos.Exif;
import p.mp3.MP3File;
import p.mp3.UtilMP3;
import p.util.Fechas;
import p.util.Util;
import p.util.UtilFile;
import p.util.UtilString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


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


    /**
     * Pone separador ([Nro]_separador[nombre]),
     * pasa a mayúscula la primer letra de cada palabra y lo demás a minúscula.
     * Si hay más de 9 archivos agregá un 0 (cero) si hay un sólo dígito antes
     * que el separador.
     */
    public void ponerSeparador(){
        RenTableModel tm = gui.getTableModel();
        List<Fila> filas = tm.getFilas();
        int tam = filas.size();
        for (int i = 1; i < tam; i++) {
            Fila fila = filas.get(i);
            if (fila.esDir() || fila.esArchivoNoMP3() ||
                (gui.soloSeleccionado() && !gui.isRowSelected(i)))
                continue;
            String str = (String)tm.getValueAt(i, RenTableModel.COLUMNA_ARCHIVO);
            str = ponerSeparador(str);
            str = mayMin(str);
            if (tam > 9 && str.indexOf(SEPARADOR) == 1)
                str = 0 + str;
            fila.setFileName(str);
        }
        tm.fireTableDataChanged();
    }

    /**
     * Agrega el nombre del archivo (sin '01_') como título del ID3
     */
    public void arch2Tit(){
        RenTableModel tm = gui.getTableModel();
        List<Fila> filas = tm.getFilas();
        int tam = filas.size();
        for (int i = 1; i < tam; i++) {
            Fila fila = filas.get(i);
            boolean b = fila.esArchivoNoMP3();
            if (fila.esDir() || b
                    || (gui.soloSeleccionado() && !gui.isRowSelected(i)))
                continue;

            String str = (String)tm.getValueAt(i, RenTableModel.COLUMNA_ARCHIVO);
            int d = str.indexOf(SEPARADOR) + 1;
            int h = str.lastIndexOf(".");
            if (d < 0 || h < 0 || h < d)
                continue;
            String tit = str.substring(d, h);
            fila.getMP3File().setTitulo(tit);
        }
        tm.fireTableDataChanged();
    }

    /**
     * Copia el título del ID3 como nombre del archivo (sin '01_')
     */
    public void tit2Arch(){
        RenTableModel tm = gui.getTableModel();
        List<Fila> filas = tm.getFilas();
        int tam = filas.size();
        for (int i = 1; i < tam; i++) {
            Fila fila = filas.get(i);
            if (fila.esDir() || fila.esArchivoNoMP3()||
                (gui.soloSeleccionado() && !gui.isRowSelected(i)) )
                continue;

            String tit = fila.getMP3File().getTitulo();
            fila.setFileName(tit + ".mp3");
        }
        tm.fireTableDataChanged();
    }

    public void grabar(boolean loguear) throws Exception {
        FileOutputStream fos = null;
        StringBuilder sb = new StringBuilder();
        try {
            String path;
            List<Fila> filas = gui.getDatosTabla();
            int cantFilas = filas.size();
            for (int i = 1; i < cantFilas; i++) {
                Fila fila = filas.get(i);
                if (fila.esDir() && !gui.modificarDirectorios())
                    continue;

                String tit = "[ " + i + "/" + cantFilas + " ] - " +
                        fila.getFileName();
                gui.setTitle(tit);
                if (fila.esMP3()){
                    MP3File mp3 = fila.getMP3File();
                    mp3.grabar();
                }

                //renombro el archivo/directorio
                if (fila.isArchModif()){
                    File file = fila.getFile();
                    path = file.getParent();

                    String nuevoNombre = path + "\\" + fila.getFileName();
                    if (!nuevoNombre.equals(file.getAbsolutePath())) {
                        File newFile = new File(nuevoNombre);
                        boolean anduvoBien = file.renameTo(newFile);
                        int cont = 1;
                        while (!anduvoBien) {
                            if (newFile.exists()) {
                                anduvoBien = file.renameTo(new File(nuevoNombre + " " + cont));
                                if (anduvoBien)
                                    nuevoNombre += " " + cont;
                                else
                                    cont++;
                            } else
                                break;
                        }

                        //deshacer
                        if (loguear) {
                            String viejo = "viejo: " + file.getAbsolutePath();
                            String nuevo = "nuevo: " + nuevoNombre;
                            if (!anduvoBien)
                                nuevo += "\n" + "^^^^^FALLÓ^^^^^^^";
                            sb.append(viejo + "\n" + nuevo + "\n" + "\n");
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        finally{
            UtilFile.logEnUserDir("desRen.txt", sb);
            gui.setTitle("");
        }
    }

    /**
     * Renombra el nombre de los archivos con un contador
     * @param str
     */
    public void renombrarFoto(String fecha, String str){
        if (str == null || fecha == null)
            return;
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        int cont = 1;
        String ext = "";

        //Cuento cuántos nombres se van a modificar para saber cuántos '0' tengo
        //que agregar antes del contador
        int cantAModificar = 0;
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (editar(i, fila)) {
                cantAModificar++;
            }
        }
        int cantDigitos = Integer.toString(cantAModificar).length();

        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;

            if (!fila.esDir()){
                String name = fila.getFile().getName();
                int p = name.lastIndexOf(".");
                if (p > 0){
                    ext = name.substring(p);
                }
            }
            String ceros = "";
            int cantDigitos2 = Integer.toString(cont).length();
            while (cantDigitos > cantDigitos2)
            {
                ceros += "0";
                cantDigitos2++;
            }

            String str2 = str;
            String fecha2 = fecha;

            if (!str2.equals(""))
                str2 = SEPARADOR + str2;
            if (!fecha2.equals(""))
                fecha2 += SEPARADOR;
            String nuevo = fecha2 + ceros + cont + str2 + ext;
            cont++;
            fila.setFileName(nuevo);
        }
        tm.fireTableDataChanged();
    }

    public void renombrar(String viejo, String nuevo){
        if (viejo == null || nuevo == null)
            return;
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            String nStr = UtilString.reemplazarTodo(str, viejo, nuevo);
            if (!str.equals(nStr))
                fila.setFileName(nStr);
        }
        tm.fireTableDataChanged();
    }

    /**
     * Devuelve true si hay que editar esta fila
     * @param i
     * @param fila
     * @return
     */
    private boolean editar(int i, Fila fila) {
        //noinspection OverlyComplexBooleanExpression
        return !((fila.esDir() && !gui.modificarDirectorios()) ||
            (gui.soloSeleccionado() && !gui.isRowSelected(i)));
    }

    public void mayusculaMinuscula(){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            str = mayMin(str);
            fila.setFileName(str);
        }
        tm.fireTableDataChanged();
    }

    public void insertarDespuesDe(String nuevo, String cond){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            fila.setFileName(
                    UtilString.insertarDespuesDe(str, nuevo, cond));
        }
        tm.fireTableDataChanged();
    }

    public void insertarAntesDe(String nuevo, String cond){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            fila.setFileName(
                    UtilString.insertarAntesDe(str, nuevo, cond));
        }
        tm.fireTableDataChanged();
    }

    public void insertarAlPrincipio(String nuevo){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = nuevo + fila.getFileName();
            fila.setFileName(str);
        }
        tm.fireTableDataChanged();
    }

    public void insertarAlFinal(String nuevo){
        insertarAlFinal(nuevo, true);
    }

    public void insertarAlFinal(String nuevo, boolean antesDeLaExtension){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            boolean adle = !fila.esDir() && antesDeLaExtension ;
            fila.setFileName(UtilString.insertarAlFinal(str, nuevo, adle));
        }
        tm.fireTableDataChanged();
    }
    /**
     * Si no encuentra 'hasta' no hace nada
     */
    public void eliminarDesdeLaIzquierda(String hasta){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            fila.setFileName(UtilString.eliminarDesdeLaIzquierda(str, hasta));
        }
        tm.fireTableDataChanged();
    }

    /**
     * Respeta la extensión
     */
    public void eliminarDesdeLaDerecha(String hasta){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            fila.setFileName(UtilString.eliminarDesdeLaDerecha(str, hasta));
        }
        tm.fireTableDataChanged();
    }

    public void deshacer(){

    }


    /**
     * Parsea el nombre del archivo para generar el título y el artista. 
     *
     * @param desde
     * @param hasta
     * @param primeroArtista
     */
    public void archivo2TitArt(String desde, String hasta, String separador, boolean primeroArtista) {
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();

        if (desde == null || hasta == null || separador == null ||
                desde.equals("")|| hasta.equals("") || separador.equals("") )
            return ;
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if ((fila.esMP3() && (
                    !gui.soloSeleccionado() || (gui.soloSeleccionado() && gui.isRowSelected(i))))){
                archivo2TitArt(fila, desde, hasta, separador, primeroArtista);
            }
        }
        tm.fireTableDataChanged();
    }



    private void archivo2TitArt(Fila fila, String desde, String hasta,String separador,  boolean primeroArtista)
    {
        String archivo = fila.getFileName();
        int d = archivo.indexOf(desde);
        int h = archivo.indexOf(hasta);
        String s = archivo.substring(d + desde.length(), h);
        System.out.println("s = " + s);
        int sep = s.indexOf(separador);
        String tit = s.substring(0, sep);
        String art = s.substring(sep + separador.length());
        if (primeroArtista){
            String tmp = tit;
            tit = art;
            art = tmp;
        }
        MP3File mp3 = fila.getMP3File();
        mp3.setArtista(art);
        mp3.setTitulo(tit);
    }



    public void borrarID3v2(String txt)
            throws IOException, UndersizedException {
        StringTokenizer st = new StringTokenizer(txt, "\n");

        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!fila.esDir()){
                if (st.hasMoreTokens()){
                    String s = st.nextToken();
                    UtilMP3.borrarID3v3(s);
                }
            }
        }
        gui.actualizarTabla();
    }

    /**
     * Renombra las filas que son archivos (no directorios) con el texto
     * de cada una de las líneas recibidas en txt
     * @param txt
     */
    public void importarNombres(String txt){
        StringTokenizer st = new StringTokenizer(txt, "\n");

        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!fila.esDir()){
                if (st.hasMoreTokens()){
                    String s = st.nextToken();
                    if (fila.esMP3() && !s.toLowerCase().endsWith(".mp3"))
                        s += ".mp3";
                    fila.setFileName(s);
                }
            }
        }
        tm.fireTableDataChanged();
    }

    public void importarNombres(File f){
        List importados;
        try{
            importados = UtilFile.getArchivoPorLinea(f);
            Iterator ite = importados.iterator();
            RenTableModel tm = gui.getTableModel();
            List<Fila> dt = gui.getDatosTabla();
            for (int i = 1; i < tm.getRowCount(); i++) {
                Fila fila = dt.get(i);
                if (gui.isRowSelected(i)){
                    if (ite.hasNext()){
                        String s = (String)ite.next();
                        fila.setFileName(s);
                    }
                }
            }
            tm.fireTableDataChanged();
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public void ponerTitulo(String txt) {
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();

        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if ((fila.esMP3() && gui.isRowSelected(i))){
                MP3File m = fila.getMP3File();
                m.setTitulo(txt);
            }
        }
        tm.fireTableDataChanged();
    }

    public void numerar() {
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        int cont = 1;

        //Cuenta cuántas nombres se van a modificar para saber cuántos '0' tengo
        //que agregar antes del contador
        int cantAModificar = 0;
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila =  dt.get(i);
            if (editar(i, fila)) {
                cantAModificar++;
            }
        }

        int cantDigitos = Integer.toString(cantAModificar).length();

        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;

            String ceros = "";
            int cantDigitos2 = Integer.toString(cont).length();
            while (cantDigitos > cantDigitos2)
            {
                ceros += "0";
                cantDigitos2++;
            }
            String str = fila.getFileName();
            String nuevo = ceros + cont + SEPARADOR + str;
            cont++;
            fila.setFileName(nuevo);
        }
        tm.fireTableDataChanged();
    }

    public void setContadorDeFotosDesde(int c){
        contadorFotosDesde = c;
    }

    public void grabarID3v2(boolean b){
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();

        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (fila.esMP3()){
                MP3File m = fila.getMP3File();
                m.agregarID3v2SiEsNecesario(b);
            }
        }
        tm.fireTableDataChanged();
    }

    public void insertarExif() {
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();

        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            String n = fila.getFileName();
            if (fila.esDir() ||
                (gui.soloSeleccionado() && !gui.isRowSelected(i)))
                continue;
            if (esFoto(n)){
                Exif ex = new Exif(fila.getFile());
                String e = UtilString.reemplazarTodo(ex.toString(), "/", "-");
                int h = n.lastIndexOf(".");
                String nse = n.substring(0, h);
                String ext = n.substring(h);

                String nn = nse + "[ " + e + " ]" + ext;
                fila.setFileName(nn);
            }
        }
        tm.fireTableDataChanged();
    }

    /**
     * Se asume que viene con la siguiete forma:
     * '01 Nommm' o '01 - nnnnn'.
     * Si hay caracteres antes del número, se borran
     */
    public static String ponerSeparador(String str){
        String res = "";

        int i = 0;
        while (str.length() >= i && !Character.isDigit(str.charAt(i)))
            i++;    //avanzo hasta el primer número

        //pongo en res el número
        char c = str.charAt(i);
        do{
            i++;
            res += c;
            if (str.length() >= i)
                c = str.charAt(i);
            else
                c = 'x';
        }
        while (Character.isDigit(c));

        if (str.length() >= i){
            str = str.substring(i);
            i = 0;
        }

        //busco el principio del nombre, salteando los posibles separadores,
        //que son " ", ".", "_", "-", ")"
        while (str.length() >= i
                && ((str.substring(i, i+1).equals(" "))
                || (str.substring(i, i+1).equals("."))
                || (str.substring(i, i+1).equals(","))
                || (str.substring(i, i+1).equals("-"))
                || (str.substring(i, i+1).equals("_"))
                || (str.substring(i, i+1).equals(")")))){
            i++;//mientras no sea ni una letra ni un número, ni un '(' => estoy en el separador
        }
        if (str.length() >= i)
            str = str.substring(i);
        res += SEPARADOR + str;

        /*for (int i = 0; i < str.length(); i++) {



            else if (Character.isLetter(c)
                    || c == '\''
                    || c == '.'
                    || c == '-'
                    || c == '_'
                    || c == ' '
                    || c == '('){
                String nom;
                if (Character.isLetter(c))
                    nom = str.substring(i).trim();
                else //si no es una letra
                    nom = str.substring((i+1)).trim();

                if (res.equals(""))
                    res = nom; //esto pasa si no tenía número
                else
                    res += SEPARADOR + nom;
                break;
            }
        } */
        return res;
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
        int cant;
        try{
            cant = Integer.parseInt(text);
        }
        catch(Exception e){
            return ;
        }
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            fila.setFileName(UtilString.eliminarCantDesdeLaDerecha(str, cant));
        }
        tm.fireTableDataChanged();
    }

    public void eliminarCantDesdeLaIzquierda(String text) {
        int cant;
        try{
            cant = Integer.parseInt(text);
        }
        catch(Exception e){
            return ;
        }
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            fila.setFileName(UtilString.eliminarCantDesdeLaIzquierda(str, cant));
        }
        tm.fireTableDataChanged();
    }

    public void insertarFecha() {
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        String fecha = "";

        int cont = 0;
        Map<String, Integer> contadorFechas = new HashMap<String, Integer>();

        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;

            Exif exif = new Exif(fila.getFile());

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
            fila.setFileName(nuevoNom);
        }
        tm.fireTableDataChanged();
    }

    public void obtenerArtistaDesdeArchivo(
            String d, String h, boolean obtenerTitulo) {

        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila =  dt.get(i);
            if (!editar(i, fila))
                continue;

            MP3File mp3 = fila.getMP3File();
            String name = mp3.getFile().getName();
            int dd = name.indexOf(d) + d.length();
            int hh = name.indexOf(h);
            if (dd > -1 && dd < hh){
                String art = name.substring(dd, hh);
                art = UtilString.mayusMinus(art);
                mp3.setArtista(art);
            }
            if (obtenerTitulo){
                dd = hh + h.length();
                hh = name.toLowerCase().indexOf(".mp3");
                if (dd > -1 && dd < hh){
                    String tit = name.substring(dd, hh);
                    tit = UtilString.mayusMinus(tit);
                    mp3.setTitulo(tit);
                }
            }
        }
        tm.fireTableDataChanged();
    }

    public void AAAAMMDD2AAAA_MM_DD() {
        RenTableModel tm = gui.getTableModel();
        List<Fila> dt = gui.getDatosTabla();
        for (int i = 1; i < tm.getRowCount(); i++) {
            Fila fila = dt.get(i);
            if (!editar(i, fila))
                continue;
            String str = fila.getFileName();
            String aaaa = str.substring(0, 4);
            String mm = str.substring(4, 6);
            String dd = str.substring(6, 8);
            String resto = str.substring(8);
            fila.setFileName(aaaa + "-" + mm + "-" + dd + " " + resto);
        }
        tm.fireTableDataChanged();
    }
}






