
package p.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@SuppressWarnings({"JavaDoc"})
public class UtilFile {
    private static final Logger logger = Logger.getLogger(UtilFile.class);

    /**
     * Devuelve una lista de File con todos los archivos que estén en el
     * directorio pasado como parámetro y el de sus subdirectorios.
     * @param directorio
     * @param filtro
     * @return
     * @throws Exception
     */
    public static List<File> archivos(File directorio,
                                    DirFileFilter filtro) throws Exception{
        return archivos(directorio, filtro, true);
    }

    public static void logEnUserDir(String archivo, StringBuilder sb) throws IOException {
        String dir = System.getProperty("user.dir");
        File file = new File(dir,archivo);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), true);

        fos.write(sb.toString().getBytes());
        fos.close();
    }

    public static List<String> archivosAsString(File directorio,
                                                FilenameFilter filtro)
            throws RuntimeException{

        return  archivosAsString(directorio, filtro, false);
    }

    /**
     * Reemplaza la lista de nombres de archivos, por el nombre con el directorio
     */
    private static void agregarDir(File directorio, List<String> files){

        String d = directorio.getAbsolutePath() + File.separator;
        for (int i = 0; i < files.size(); i++) {
            files.set(i, d + files.get(i));
        }
    }

    /**
     * Devuelve una lista con los nombres de los archivos dentro del directorio pasado como
     * parámetro.
     */
    public static List<String> archivosAsString(File directorio,
                                                FilenameFilter filtro,
                                                boolean recursivo)
            throws RuntimeException{

        if (!directorio.exists())
            throw new RuntimeException("No existe el directorio " + directorio.getAbsolutePath());

        List<String> res = new ArrayList<String>();
        //Guardo los archivos del directorio actual
        String[] files;
        if (filtro != null)
            files = directorio.list(filtro);
        else{
            DirFileFilter f = new DirFileFilter(false, true ,true);
            files = directorio.list(f);
        }
        List<String> nombres = Arrays.asList(files);
        agregarDir(directorio, nombres);
        res.addAll(nombres);

        if (recursivo){
            //hago recursión por los subdirectorios
            DirFileFilter filtroDir = new DirFileFilter(true);
            File[]  subd = directorio.listFiles(filtroDir);
            for (File d : subd) {
                List<String> tem = archivosAsString(d, filtro, recursivo);
                res.addAll(tem);
            }
        }
        return res;
    }

    /**
     * Devuelve una lista de File con todos los archivos que estén en el
     * directorio pasado como parámetro y el de sus subdirectorios si
     * 'recursivo' = true.
     * @param directorio
     * @param filtro
     * @return
     * @throws Exception
     */
    public static List<File> archivos(String directorio,
                                    DirFileFilter filtro,
                                    boolean recursivo) throws Exception{
        return archivos(new File(directorio), filtro, recursivo);
    }
    /**
     * Devuelve una lista de File con todos los archivos que estén en el
     * directorio pasado como parámetro y el de sus subdirectorios si
     * 'recursivo' = true.
     * @param directorio
     * @param filtro
     * @return
     * @throws Exception
     */
    public static List<File> archivos(File directorio,
                                    DirFileFilter filtro,
                                    boolean recursivo) throws Exception{

        if (!directorio.exists()) 
            throw new Exception("No existe el directorio " + directorio.getAbsolutePath());

        List<File> res = new ArrayList<File>();
        //Guardo los archivos del directorio actual
        ;
        if (filtro == null)
            filtro = new DirFileFilter(false, true ,true);


        if (recursivo){
            //si hay que mostrar archivos => los muestro primero y luego hago la recursión
            if (!filtro.isSoloDirectorios()){
                boolean isSoloArchivosOriginal = filtro.isSoloArchivos();
                filtro.setSoloArchivos(true);
                File[] files = directorio.listFiles(filtro);
                if (files != null){
                    List<File> archivos = Arrays.asList(files);
                    Collections.sort(archivos);
                    res.addAll(archivos);
                }
                filtro.setSoloArchivos(isSoloArchivosOriginal);
            }

            //hago recursión por los subdirectorios
            DirFileFilter filtroDir = new DirFileFilter(true);
            if (filtro != null){
                filtroDir.addExcluirDirsCaseUnsensitive(filtro.getExcluirDirsCaseUnsensitive());
                filtroDir.addExcluirDirsEmpiezanCon(filtro.getExcluirDirsEmpiezanCon());
            }

            File[]  subd = directorio.listFiles(filtroDir);
            if (subd != null) {
                List<File> archivos = Arrays.asList(subd);
                Collections.sort(archivos);
                for (File d : archivos) {
                    List<File> tem = archivos(d, filtro, recursivo);
                    //primero meto el padre y luego los hijos
                    res.add(d);
                    res.addAll(tem);
                }
            }
        }
        else{
            File[] files = directorio.listFiles(filtro);
            List<File> archivos = Arrays.asList(files);
            Collections.sort(archivos);
            //primero pongo los directorios
            for(File a : archivos) {
                if (a.isDirectory())
                    res.add(a);
            }
            //y luego los archivos
            for(File a : archivos) {
                if (a.isFile())
                    res.add(a);
            }
        }
        return res;
    }

    public static List<File> archivos(File directorio) throws Exception{
        return archivos(directorio, null);
    }

    public static List<String> directorios(String d){
        return directorios(new File(d));
    }
    public static List<String> directorios(File d){
        return directorios(d, null);
    }

    public static List<String> directorios(String d, List<String> excluir){
        return directorios(new File(d), excluir);
    }

    public static List<String> directorios(File f, List<String> excluir){
        List<String> res = new ArrayList<String>();
        List<String> res2 = new ArrayList<String>();
        if (!f.exists())
            throw new RuntimeException("No existe el archivo " + f.getAbsolutePath());

        if (excluir != null){
            String dir = f.getName();
            if (excluir.contains(dir))
                return res;
        }

        DirFileFilter filtro = new DirFileFilter(true);
        File[]  subd = f.listFiles(filtro);
        res2.add(f.getAbsolutePath());
        if (subd != null && subd.length > 0){
            for (File d : subd) {
                List<String> tem = directorios(d, excluir);
                res2.addAll(tem);
            }
        }
        Object[] o = Util.ordenar(res2);
        for (Object anO : o) {
            res.add((String) anO);
        }

        return res;
    }

    /**
     * Borra el directorio y lo que tiene adentro
     */
    public static void borrarDirectorio(String dir) throws Exception{
        //creo el directorio temporal donde se va a mover el contenido
        String dt = "C:\\Temp\\borrar\\" + System.currentTimeMillis();
        File f = new File(dt);
        f.mkdirs();
        moverContenidoDeDir(dir, dt);
    }

    public static void copiar( String origen, String destino )
        throws Exception{

        copiar(new File(origen), new File(destino));
    }

    public static void copiar(File entrada, File salida, long desde, long hasta)
            throws Exception {
        FileInputStream fis ;
        FileOutputStream fos ;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try{
            //Creo, si no existe, el directorio de salida
            salida.getParentFile().mkdirs();


            fis = new FileInputStream(entrada);
            fos = new FileOutputStream(salida);
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            long tamEnt = entrada.length();
            if (tamEnt > desde)
                fis.skip(desde);
            if (tamEnt < hasta)
                hasta = tamEnt;
            int nuevoTam = (int) ((int)hasta - desde);
            byte[] leer = new byte[nuevoTam];
            bis.read(leer);
            bos.write(leer);
        }finally{
            if (bos != null)
                bos.close();
            if (bis != null)
                bis.close();
        }
    }
    
    public static void copiar( File origen, File destino )
        throws IOException{
      BufferedInputStream in;
      BufferedOutputStream out;
      int data;


      if (origen.isDirectory()){
          System.out.println("Error: el origen de lo que se quiere copiar es un directorio");
          return;
      }

      // Open the input file.  A BufferedInputStream is used here to make
      // the copy more efficient.  The copy could have been done
      // using the FileInputStream only.

      // The FileInputStream was used instead of a FileReader since the
      // program is interested in copying bytes and not characters/strings.
      in = new BufferedInputStream(new FileInputStream(origen));

      //creo el directorio si no existe
      if (!destino.getParentFile().exists())
          destino.getParentFile().mkdirs();
      // If the output file exists, it will be overwritten.
      out = new BufferedOutputStream(new FileOutputStream(destino));

      // Now copy the files one byte at a time (note both in and
      // out are open at this point in the program)
      // Copy the files a byte at a time
      while ( (data = in.read()) != -1) {
          out.write(data);
      }
      in.close();
      out.close();

        //
        destino.setLastModified(origen.lastModified());
        destino.setWritable(origen.canWrite());
    }

    public static void moverContenidoDeDir(File raiz, File dest) throws Exception {
        moverContenidoDeDir(raiz, dest, true);
    }

    public static void moverContenidoDeDir(File raiz, File dest, boolean compararAntesDeBorrar) throws Exception {
        if (!raiz.exists())
            return;

        File[] hijos = raiz.listFiles();
        for (File origen : hijos) {
            File destino = new File(dest, origen.getName());
            if (origen.isFile()){
                if (compararAntesDeBorrar){
                    copiar(origen, destino);
                    boolean sonIguales = FileUtils.contentEquals(origen, destino);
                    if (!sonIguales)
                        throw new RuntimeException("No se borra el archivo origen (" + origen.getAbsolutePath() +
                                " ) porque es distinto del destino (" + destino.getAbsolutePath() + " ). ");
                    else {
                        //todo borrar
                        System.out.println("Son iguales y se borran: ");
                        System.out.println("origen: " + origen.getAbsolutePath());
                        System.out.println("destino: " + destino.getAbsolutePath());
                        origen.delete();
                    }
                }
                else
                    origen.renameTo(destino);
            }
            else {
                destino.mkdirs();
                moverContenidoDeDir(origen.getAbsolutePath(), destino.getAbsolutePath());
                origen.delete();
            }
        }
    }

    public static void moverContenidoDeDir(String origen, String dest) throws Exception {
        moverContenidoDeDir(origen, dest, true);
    }

    public static void moverContenidoDeDir(String origen, String dest, boolean compararAntesDeBorrar) throws Exception {
        moverContenidoDeDir(new File(origen), new File(dest), compararAntesDeBorrar);
    }

    public static void copiarDir(String origen, String dest)
        throws Exception {
        copiarDir(new File(origen), new File(dest));
    }

    public static void copiarDir(File origen, File dest)
            throws Exception {
        if (!origen.exists()){
            throw new Exception("No existe el directorio origen: " + origen);
        }

        File[] hijos = origen.listFiles();
        for (File f : hijos) {
            if (f.isFile())
                copiar(f, new File(dest, f.getName()));
            else {
                File ff = new File(dest, f.getName());
                ff.mkdirs();
                copiarDir(f.getAbsolutePath(), ff.getAbsolutePath());
            }
        }
    }
    /**
     * @param f
     * @return el tamaño del archivo o directorio en bytes
     */
    public static long getSize(File f){
        long res = 0;
        if (f.isFile())
            res = f.length();
        else{
            File[] hijos = f.listFiles();
            for (File hijo : hijos) {
                res += getSize(hijo);
            }
        }
        return res;
    }

	public static List<String> getArchivoPorLinea(String f) throws IOException {
        return getArchivoPorLinea(f, null);
    }

	public static List<String> getArchivoPorLinea(String f, String excluirSiEmpiezaCon) throws IOException {
        return getArchivoPorLinea(new File(f.trim()), excluirSiEmpiezaCon);
    }

    public static List<String> getArchivoPorLinea(File f) throws IOException {
        return getArchivoPorLinea(f, null);
    }

    public static List<String> getArchivoPorLinea(String f, boolean sacarLineasVacias) throws IOException {

        return getArchivoPorLinea(new File(f.trim()), sacarLineasVacias);
    }

    public static List<String> getArchivoPorLinea(File f, boolean sacarLineasVacias) throws IOException {
        return getArchivoPorLinea(f, Integer.MAX_VALUE, null, sacarLineasVacias);
    }

    public static List<String> getArchivoPorLinea(File f, String excluirSiEmpiezaCon) throws IOException {
        if (!f.exists()){
            String s = "No existe el archivo = '" + f.getAbsolutePath() + "'";
            JOptionPane.showMessageDialog(null, s);
            System.out.println(s);
            throw new IOException("No existe el archivo = '" + f.getAbsolutePath() + "'");
        }

        return getArchivoPorLinea(f, Integer.MAX_VALUE, excluirSiEmpiezaCon);
    }

    public static List<String> getArchivoPorLinea(File f, int cantMaxLineas)
			throws IOException{
        return getArchivoPorLinea(f, cantMaxLineas, null);
    }

    public static List<String> getArchivoPorLinea(File f, int cantMaxLineas,
                                                  String excluirSiEmpiezaCon)
			throws IOException{
        return getArchivoPorLinea(f, cantMaxLineas, excluirSiEmpiezaCon, false);
    }

    public static List<String> getArchivoPorLinea(File f, int cantMaxLineas,
                                                  String excluirSiEmpiezaCon, boolean sacarLineasVacias)
			throws IOException{
        LineNumberReader lnr = null;
        int cont = 0;
        try{
            List<String> res = new ArrayList<String>();
            lnr = new LineNumberReader(new FileReader(f));
            while(lnr.ready() && cont <cantMaxLineas){
                String linea = lnr.readLine();
                if (excluirSiEmpiezaCon != null && linea.startsWith(excluirSiEmpiezaCon))
                    ;
                else
                    if (!sacarLineasVacias || !linea.trim().equals("")){
                        res.add(linea);
                        cont++;
                    }
            }
            return res;
        }
        finally{
            if (lnr != null)
                lnr.close();
        }
    }


    public static void readFile(File f, LineNumberReaderItf reader)
			throws IOException{
        readFile(f, reader, 0, Integer.MAX_VALUE);
    }

    public static void readFile(File f, LineNumberReaderItf reader,
                                int desdeLaLinea , int cantMaxLineas)
			throws IOException{
        LineNumberReader lnr = null;
        FileInputStream fis = null;
        InputStreamReader is = null;
        int cont = 0;
        try{
            fis = new FileInputStream(f);
            is = new InputStreamReader(fis, "ISO-8859-1");// no le da bola
            lnr = new LineNumberReader(is);
            while(lnr.ready() && cont <cantMaxLineas){
                String linea = lnr.readLine();
                if (desdeLaLinea <= cont)
                    reader.newLine(linea);
                cont++;
            }
        }
        finally{
            if (is != null)
                is.close();
            if (fis != null)
                fis.close();
            if (lnr != null)
                lnr.close();
            reader.terminó();
        }
    }


    /**
     * Une los archivos recibidos en la lista y lo guarda en el
     * archivo 'salida' (que debería no existir)
     * @param archivos
     * @param salida
     * @throws java.io.IOException
     */
    public static void unir(List<File> archivos, File salida) throws IOException
    {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        int data;


        if (salida.exists()){
            String msg = "Error: el archivo de salida [" +
                  salida.getAbsolutePath() + "] ya existe";
            System.out.println(msg);
            return;
        }
        //creo el directorio de salida (si no existe)
        if (!salida.getParentFile().exists())
            salida.getParentFile().mkdirs();

        try{
            out = new BufferedOutputStream(new FileOutputStream(salida));

            for (File file : archivos) {
                in = new BufferedInputStream(new FileInputStream(file));
                while ((data = in.read()) != -1) {
                    out.write(data);
                }
                in.close();
            }
        }catch(FileNotFoundException ioe){
            logger.error(ioe.getMessage(), ioe);
            throw  ioe;
        }
        finally{
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }

    /**
     * Si el archivo destino existe => lo reemplaza
     * @param txt
     * @param destino
     */
    public static void crearArchivo(String txt, String destino)
            throws IOException {
        File d = new File(destino);
        d.createNewFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destino, false);
            fos.write(txt.getBytes());
            fos.close();
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw ex;
        }
        finally{
            if (fos != null)
                fos.close();
        }
    }

    /**
     * Si el archivo destino no existe => lo crea
     * @param txt
     * @param destino
     */
    public static void agregarAlFinal(String txt, String destino) throws IOException {
        System.out.println("txt = " + txt);

        System.out.println("destino = " + destino);
        agregarAlFinal(txt, new File(destino));
    }

    /**
     * Si el archivo destino no existe => lo crea
     * @param txt
     * @param destino
     */
    public static void agregarAlFinal(String txt, File destino)
            throws IOException {
        if (destino.exists())
            txt = Constantes.NUEVA_LINEA + txt;
        else
            destino.createNewFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destino, true);

            /////////////////////
//            SortedMap<String, Charset> a = Charset.availableCharsets();
//            for (String s : a.keySet()) {
//                try {
//                    Charset charset = a.get(s);
//                    String xxxxx = new String(txt.getBytes(charset));
//                    boolean ig = xxxxx.equals(txt);
////                    System.out.println("xxxxx.equals(txt) = " + ig);
//                    if (ig)
//                        System.out.println(s + " =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + charset);
//                    else
//                        System.out.println(s + " = " + charset);
//                } catch (Exception e) {
//                    System.out.println("e.getMessage() = " + e.getMessage());
//                }
//            }
            ////////////////
            fos.write(txt.getBytes());
            fos.close();
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw ex;
        }
        finally{
            if (fos != null)
                fos.close();
        }
    }

    public static void guardartArchivoPorLinea(String nombreArch, List<String> datos)
			throws IOException{
        guardartArchivoPorLinea(new File(nombreArch), datos);
    }

    public static void guardartArchivoPorLinea(File f, List<String> datos)
			throws IOException{
        guardartArchivoPorLinea(f, datos, false);
    }

    /**
     * Si existe el archivo => agrega al final.
     * @param f
     * @param datos
     * @throws IOException
     */
    public static void guardartArchivoPorLinea(File f, List<String> datos,
                                               boolean reemplazarArchivoExistente)
			throws IOException{
        guardartArchivoPorLinea(f, datos, reemplazarArchivoExistente, 0, datos.size()-1);
    }

    /**
     * Si existe el archivo => agrega al final.
     * @param f
     * @param datos
     * @throws IOException
     */
    public static void guardartArchivoPorLinea(File f, List<String> datos,
                                               boolean reemplazarArchivoExistente,
                                               int desdeLaLinea, int hastaLaLinea)
			throws IOException{

        FileOutputStream fos;
        if (f.exists()){
            fos = new FileOutputStream(f,!reemplazarArchivoExistente);
        }
        else {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("No se pudo crear el archivo: " + f.getAbsolutePath());
                throw new RuntimeException(e);
            }
            fos = new FileOutputStream(f);
        }
        try {
            for(int i = desdeLaLinea; i <hastaLaLinea +1; i++) {
                String s = datos.get(i) + Constantes.NUEVA_LINEA;
                fos.write(s.getBytes());
            }
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw ex;
        }
        finally{
            fos.close();
        }
    }

    public static void partir(String entrada, int cantArchSalida) throws IOException {
        File fileEntrada = new File(entrada);
        partir(fileEntrada, cantArchSalida, fileEntrada.getParentFile());
    }

    public static void partir(File entrada, int cantArchSalida, File dirSalida) throws IOException {
        int tamXArch = (int) (entrada.length() / cantArchSalida);
        byte[] bytes = new byte[tamXArch];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(entrada) );
        String nombre = entrada.getName();
        String ext = "";
        int i1 = nombre.lastIndexOf(".");
        if (i1 > 0){
            nombre = nombre.substring(0, i1) + "_";
            ext = entrada.getName().substring(i1);
            System.out.println("nombre = " + nombre);
            System.out.println("ext = " + ext);
        }

        for(int i = 0; i < cantArchSalida; i++) {
            bis.read(bytes);
            dirSalida.mkdirs();

            //agrego un cero si hay más de 10 particiones y si i es < 10
            String sufijo = ""+i;
            if (cantArchSalida > 10 && i < 10)
                sufijo = "0" + sufijo;

            File fileSalida = new File(dirSalida, nombre + sufijo + ext);
            fileSalida.createNewFile();
            FileOutputStream fos = new FileOutputStream(fileSalida);
            fos.write(bytes);
            boolean seguir = true;
            while(seguir){
                int by = bis.read();
                if (by == 10){
                    seguir = false;
                }else{
                    seguir = bis.available() > 0;
                }
                fos.write(by);
            }
            //si es el último=> meto lo que falta
            if (i == cantArchSalida-1){
                bytes = new byte[bis.available()];
                bis.read(bytes);
                fos.write(bytes);
            }
            fos.close();
        }
    }

    public static void main(String[] args) throws Exception {
        SortedMap<String, Charset> a = Charset.availableCharsets();
        for (String s : a.keySet()) {
            System.out.println(s + " = " + a.get(s));
        }
        String origen = "D:\\TMP\\queryReporte.txt";
        String x = getContenidoAsString(origen);
        System.out.println("x = " + x);

//        String destino = "D:\\P\\videos\\youtube\\tmp";
//        moverContenidoDeDir(origen, destino, true);
//        if (true)
//            return ;
//        File f = new File(destino);
//        File[] files = f.listFiles();
//
//        //partirMP3(new File(origen, "05-07_orig - copia.mp3x"), 10, new File(destino));
//        unir(Arrays.asList(files), new File(destino,  "union.mp3x"));

//        if (args.length != 2){
//            System.out.println("Modo de uso: java UtilFile NOMBRE_DE_ARCHIVO CANT_DE_PARTES");
//            System.exit(-1);
//        }
//        if (args.length > 0){
//            if (args[0].equalsIgnoreCase("-partir")){
//                String entrada = args[1]; //"D:\\ADN.log";
//                int cantDeLXA = Integer.parseInt(args[2]) ; //"D:\\ADN.log";
//                System.out.println("Se partirá el archivo = '" + entrada + "'");
//                System.out.println("en archivos de '" + cantDeLXA + "' líneas");
//                int cantDeArch = partirPorLineas(entrada, cantDeLXA);
//                System.out.println("Cantidad de archivos generados = " + cantDeArch);
//            }
//        }
//        String ent = args[0];
//        int cant = Integer.parseInt(args[1]);
//        System.out.println("ent = " + ent);
//        System.out.println("cant = " + cant);
//        partir(ent, cant);

//        String sal = "D:\\temp\\_fact\\p2.sql";
//        List<String> archivoPorLinea = getArchivoPorLinea(new File(entrada), 5000);
//        guardartArchivoPorLinea(new File(sal), archivoPorLinea);
//        partirPorLineaVacia(entrada);
//        cantLineas(entrada);
//        partirEnDosPorLineas(entrada, 1000);
//
//        tmp(entrada);
//        partirPorLineas(file, 1000);
//        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
//        int cont = 0;
//        while(lnr.ready() ){
//            String linea = lnr.readLine();
//            if (linea == null || linea.length() < 5){
//                System.out.println("linea: " + cont + ": '" + linea + "'");
//            }
//            cont++;
//        }
        System.out.println("Listo");
    }

    public static void partirMP3(File entrada, int cantArchSalida, File dirSalida) throws IOException {

        int tamXArch = (int) (entrada.length() / cantArchSalida);
        byte[] bytes = new byte[tamXArch];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(entrada) );
        String nombre = entrada.getName();
        String ext = "";
        int i1 = nombre.lastIndexOf(".");
        if (i1 > 0){
            nombre = nombre.substring(0, i1) + "_";
            ext = entrada.getName().substring(i1);
            System.out.println("nombre = " + nombre);
            System.out.println("ext = " + ext);
        }

        boolean agregarSync = false;
        for(int i = 0; i < cantArchSalida; i++) {
            if (i == cantArchSalida-1){
                bytes = new byte[bis.available()];
            }

            bis.read(bytes);
            dirSalida.mkdirs();

            //agrego un cero al nombre del archivo si hay más de 10 particiones y si i es < 10
            String sufijo = ""+i;
            if (cantArchSalida > 10 && i < 10)
                sufijo = "0" + sufijo;

            File fileSalida = new File(dirSalida, nombre + sufijo + ext);
            fileSalida.createNewFile();
            FileOutputStream fos = new FileOutputStream(fileSalida);

            if (agregarSync){
                agregarSync = false;
                fos.write(255);
                fos.write(251);
                fos.write(16);
                fos.write(196);
            }
            fos.write(bytes);
            boolean seguir = true;


            //intento seguir copiando hasta el sync de mp3

            if(bis.available() > 3 && i < cantArchSalida-1){
                int by = bis.read();
                int by2 = bis.read();
                int by3 = bis.read();
                int by4 = bis.read();
                int cont = 4;
                while(seguir){
                    if (by == 255 && by2 == 251 && by3 == 16 && by4 == 196){
                        seguir = false;
                        agregarSync = true;
                        System.out.println("Encontré FFFB, cont = " + cont);
                    }else{
                        fos.write(by);
                        if ( bis.available() > 0){
                            by = by2;
                            by2 = by3;
                            by3 = by4;
                            by4 = bis.read();
                            cont++;
                        }
                        else {
                            seguir = false;
                            fos.write(by2);
                            fos.write(by3);
                            fos.write(by4);
                            System.out.println("NO Encontré FFFB");
                        }
                    }
                }
            }
            //si es el último=> meto lo que falta
            if (i == cantArchSalida-1 && bis.available() > 0){
                bytes = new byte[bis.available()];
                bis.read(bytes);
                fos.write(bytes);
            }
            fos.close();
        }
    }

    //Devuelve la cantidad de archivos generados
    public static int partirPorLineas(String entrada, int cantDeLineasPorArchivo)
            throws IOException {
        File fileEntrada = new File(entrada);

        LineNumberReader lnr = new LineNumberReader(new FileReader(entrada) );
        List<String> lineas = new ArrayList<String>(cantDeLineasPorArchivo);

        int nroLinea = 0;
        int contadorDeArchivo = 1;

        while (lnr.ready()){
            lineas.add(lnr.readLine());
            nroLinea++;
            if (nroLinea == cantDeLineasPorArchivo){
                String nom = fileEntrada.getName() + "_" + contadorDeArchivo;
                File newFile = new File(fileEntrada.getParent(), nom);
                guardartArchivoPorLinea(newFile, lineas);
                lineas.clear();
                contadorDeArchivo++;
                nroLinea = 0;
            }
        }
        if (lineas.size()>0){
            String nom = fileEntrada.getName() + "_" + contadorDeArchivo;
            File newFile = new File(fileEntrada.getParent(), nom);
            guardartArchivoPorLinea(newFile, lineas);
        }
        else
            contadorDeArchivo--;
        return contadorDeArchivo;
    }


    public static void partirEnDosPorLineas(String entrada, int cantDeLineasPrimerArchivo)
            throws IOException {
        File fileEntrada = new File(entrada);

        LineNumberReader lnr = new LineNumberReader(new FileReader(entrada) );
        List<String> lineas = new ArrayList<String>(cantDeLineasPrimerArchivo);

        int nroLinea = 0;
        int contadorDeArchivo = 1;
        int cantLineas = cantDeLineasPrimerArchivo;
        while (lnr.ready()){
            lineas.add(lnr.readLine());
            nroLinea++;
            if (nroLinea == cantLineas){
                String nom = fileEntrada.getName() + "_" + contadorDeArchivo;
                File newFile = new File(fileEntrada.getParent(), nom);
                guardartArchivoPorLinea(newFile, lineas);
                lineas.clear();
                contadorDeArchivo++;
                nroLinea = 0;
                cantLineas = Integer.MAX_VALUE;
            }
        }
        if (lineas.size()>0){
            String nom = fileEntrada.getName() + "_" + contadorDeArchivo;
            File newFile = new File(fileEntrada.getParent(), nom);
            guardartArchivoPorLinea(newFile, lineas);
        }
    }


    /**
     * Si recibe un archivo, lo guarda. Si recibe un directorio, guarda todos
     * los archivos de ese directorio, y llama recursivamente a sus subdirectorios
     */
    public static void zipear(String pAZipear, String pSalida) throws IOException {
        if (pAZipear != null && !pAZipear.trim().equals("")){
            File dirObj = new File (pAZipear);
            if (dirObj.exists()) {
                if (dirObj.isDirectory()) {
                    File sal = new File(pSalida);
                    zipear(pAZipear,  sal);
                }else
                    zipFunc(pAZipear, pSalida);
            }else
                System.out.println (pAZipear + " no existe.");
        }
    }

    private static void zipear(String pDirAZipear, File pSalida) throws IOException {
        File dirObj = new File(pDirAZipear);
        FileOutputStream fos = new FileOutputStream(pSalida);
        ZipOutputStream  zos = new ZipOutputStream(fos);
        BufferedInputStream bis = null;

        try{
            File [] fileList = dirObj.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory())
                    zipear(aFileList.getPath(), pSalida);
                else if (aFileList.isFile()) {
                    String path = aFileList.getPath();
                    FileInputStream fis = new FileInputStream(path);
                    bis = new BufferedInputStream(fis);
                    zipear(path, zos, bis);
                }
            }
        }
        finally{
            if (bis != null) {
                bis.close();
            }
            fos.close();
            zos.close();
        }
    }

    private static void zipFunc (String filePath, String pSalida) throws IOException {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

		try {
			FileInputStream fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);
            fos = new FileOutputStream(pSalida);
            zos = new ZipOutputStream(fos);

            zipear(filePath, zos, bis);

        }
        finally{
            if (bis != null) {
                bis.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (zos != null) {
                zos.close();
            }
        }
	}

    private static void zipear(String filePath, ZipOutputStream zos,
                               BufferedInputStream bis) throws IOException {
        // Create a Zip Entry and put it into the archive (no data yet).
        ZipEntry fileEntry = new ZipEntry(filePath.substring(3));
        zos.putNextEntry(fileEntry);

        // Create a byte array object named data and declare byte count variable.
        byte[] data = new byte[1024];
        int byteCount;
        // Create a loop that reads from the buffered input stream and writes
        // to the zip output stream until the bis has been entirely read.
        while ((byteCount = bis.read(data, 0, 1024)) > -1) {
            zos.write(data, 0, byteCount);
        }
        zos.flush();
    }


    /**
     * Descomprime todos los zip que estén dentro del dir pasado como parámetro. Deja el contenido del zip en el mismo
     * directorio en donde está el zip
     * @param dir
     * @throws IOException
     */
    public static void deszipear(String dir) throws Exception {
        DirFileFilter f = new DirFileFilter(false, "zip");
        List<File> files = archivos(dir, f, true);
        for (File file : files) {
            deszipear(new ZipFile(file), file.getParent());
        }
    }

    public static void deszipear(ZipFile zipFile, String dirSalida) throws IOException {
        Enumeration entries;
        try {
          entries = zipFile.entries();

          while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();

            if(entry.isDirectory()) {
              // Assume directories are stored parents first then children.
              System.err.println("Extracting directory: " + entry.getName());
              // This is not robust, just for demonstration purposes.
              (new File(entry.getName())).mkdir();
              continue;
            }

            System.err.println("Extracting file: " + entry.getName());
              File archSal = new File(dirSalida,  entry.getName());
              copyInputStream(zipFile.getInputStream(entry),
                new BufferedOutputStream(new FileOutputStream(archSal)));
          }

          zipFile.close();
        } catch (IOException ioe) {
          System.err.println("Unhandled exception:");
          logger.error(ioe.getMessage(), ioe);
        }
    }

    private  static void copyInputStream(InputStream in, OutputStream out)
      throws IOException
      {
        byte[] buffer = new byte[1024];
        int len;

        while((len = in.read(buffer)) >= 0)
          out.write(buffer, 0, len);

        in.close();
        out.close();
      }


    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public static boolean deleteAll(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for(String aChildren : children) {
                boolean success = deleteAll(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }



    private static void partirPorLineaVacia(String entrada) throws IOException {

        File fileEntrada = new File(entrada);
        LineNumberReader lnr = new LineNumberReader(new FileReader(entrada) );
        List<String> lineas = new ArrayList<String>();
        int nroLinea = 0;
        int contadorDeArchivo = 1;

        while (lnr.ready()){
            String s = lnr.readLine();

            if (s.trim().equals("")){
                String nom = fileEntrada.getName() + "_" + contadorDeArchivo;
                File newFile = new File(fileEntrada.getParent(), nom);
                guardartArchivoPorLinea(newFile, lineas);
                lineas.clear();
                contadorDeArchivo++;
                nroLinea = 0;
            }
            else{
                if (lineas.size() < 5000){
                    lineas.add(s);
                    nroLinea++;
                }
            }

        }
        if (lineas.size()>0){
            String nom = fileEntrada.getName() + "_" + contadorDeArchivo;
            File newFile = new File(fileEntrada.getParent(), nom);
            guardartArchivoPorLinea(newFile, lineas);
        }
    }

    private static void buscarLineaVacia(String entrada) throws IOException {

        File fileEntrada = new File(entrada);

        LineNumberReader lnr = new LineNumberReader(new FileReader(entrada) );
        List<String> lineas = new ArrayList<String>();

        int nroLinea = 0;

        while (lnr.ready()){
            String s = lnr.readLine();
            nroLinea++;
            if (s.trim().equals("")){
                System.out.println("Línea vacía: " + nroLinea);
                return ;
            }
        }
        System.out.println("lleno: nroLinea = " + nroLinea);
    }

    private static void mostrarUnPoco(String entrada) throws IOException {

        File fileEntrada = new File(entrada);

        LineNumberReader lnr = new LineNumberReader(new FileReader(entrada) );
        List<String> lineas = new ArrayList<String>();

        int nroLinea = 0;

        while (lnr.ready()){
            lnr.readLine();
            nroLinea++;
            if (nroLinea % 100000 == 0){
                System.out.println("___");
                System.out.println("nroLinea = " + nroLinea);
                for(int i = 0; i < 10; i++){
                    if(lnr.ready()){
                        System.out.println(lnr.readLine());
                        lnr.readLine();
                        nroLinea++;
                    }
                }
                System.out.println("-------------");
            }
        }
        System.out.println("nroLinea = " + nroLinea);
    }

    private static void cantLineas(String entrada) throws IOException {

        File fileEntrada = new File(entrada);

        LineNumberReader lnr = new LineNumberReader(new FileReader(entrada) );

        int nroLinea = 0;

        while (lnr.ready()){
            lnr.readLine();
            nroLinea++;

        }
        System.out.println("nroLinea = " + nroLinea);
    }

    private static void borrarHasta(String entrada) throws IOException {

        File fileEntrada = new File(entrada);
        LineNumberReader lnr = new LineNumberReader(new FileReader(entrada) );

        File f = new File(entrada + "_xxx");
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);

        int nroLinea = 0;
        boolean grabarSal = false;
        while (lnr.ready()){
            String s = lnr.readLine();
            if (s.equals("INSERT INTO FCTL_REL_SERV_CONCEPTO_CST ( RSCS_TIPO_SERVICIO_ADIC_ID, RSCS_CONCEPTO_COSTEO_ID,")){
                lnr.readLine();//RSCS_ESTADO, RSCS_USUARIO_ALTA, RSCS_FECHA_ALTA, RSCS_USUARIO_ULT_MODIF, RSCS_FECHA_ULT_MODIF,
                lnr.readLine();//  TIMESTAMP ) VALUES (
                lnr.readLine();//  'MO', 'A', 'A', 'OPERACIONES',  TO_Date( '04/10/2008 05:45:26 PM', 'MM/DD/YYYY HH:MI:SS AM')
                lnr.readLine();  //, NULL,  TO_Date( '04/10/2008 05:45:26 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '04/10/2008 05:45:26 PM', 'MM/DD/YYYY HH:MI:SS AM'));
                lnr.readLine();  //commit;
                lnr.readLine();
                grabarSal = true;
            }
            if (grabarSal){
                String x = s + Constantes.NUEVA_LINEA;
                fos.write(x.getBytes());
            }
        }

            fos.close();
    }

    public static void sacar(File fileEntrada, File fileSalida, int desde, int hasta) throws IOException {
        fileSalida.createNewFile();
        FileOutputStream fos = new FileOutputStream(fileSalida);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileEntrada) );

        //grabo desde el principio hasta la parte a eliminar
        byte[] bytes = new byte[desde];
        bis.read(bytes);
        fos.write(bytes);

//        //grabo desde el 'hasta' hasta el final del archivo
//        bytes = new byte[(int)fileEntrada.length() - hasta];
//        bis.skip(hasta);
//        bis.read(bytes);
//        fos.write(bytes);

        //prueba: grabo lo cortado
//        byte[] bytes = new byte[(int)hasta-desde];
//        bis.skip(desde);
//        bis.read(bytes);
//        fos.write(bytes);


        fos.close();
        bis.close();
    }

    public static String getContenidoAsString(String file) throws IOException {
        FileInputStream fis = null ;

        try{
            fis = new FileInputStream(file);

            int tamEnt = fis.available();
            byte[] leer = new byte[tamEnt];
            fis.read(leer);
            return  new String(leer);
        }finally{
            if (fis != null)
                fis.close();
        }
    }
}
