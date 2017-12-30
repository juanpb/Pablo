package p.aplic.subtitulos.viejo;

import org.apache.log4j.Logger;
import p.aplic.subtitulos.Subtitulo;
import p.util.Constantes;
import p.util.Hora;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * User: JPB
 * Date: Apr 28, 2009
 * Time: 8:06:33 AM
 */
public class Subtitulos {
    private static final Logger logger = Logger.getLogger(Subtitulos.class);
    //Formato 1: .sub
    //HH:MM:SS:texto
    private static int _formatoOriginal = 1;
    private static int _formatoSalida   = 1;

    private static Vector _subtitulos = null;
    private static long _inicioS = (new Hora(0,0,49,0)).todo;
    private static long _finS = (new Hora(1,19,14,0)).todo;
    private static String _fileName = "";
    private static final String SALTO_LINEA = "|";
    private static final long _duracionSubs = (long)2000;

    public Subtitulos(){}

    public Subtitulos(String fileName) {

        _fileName = fileName;
    }

    public static void main(String[] args) throws Exception{

//        String dir = "F:\\_videos_\\Películas\\Sin ver\\_____grabadas\\Hiroshima mon amour (1959) [JAP-FRA]";
//        String arch = "conMS.srt";
//        String x = dir + "\\" +  arch;
//        Subtitulos s = new Subtitulos(x);


//        s.formato1Aformato2();
//        Hora horaDesde = new Hora(0,0,4,0);
//        Hora horaHasta = new Hora(0,53,0,0);
//        _formatoOriginal = 2;
//        estirar(new File(x), horaDesde, horaHasta);
//          guardarFormato2();
        /****************offset*****************/
//        parsearFormato2();
//        offset(0, 0, -2, 00);
//         guardarFormato2();
        /****************offset*****************/

        /**********      Estirar                *********************/
//        Hora horaDesde = new Hora(0,0,10,0);
//        Hora horaHasta = new Hora(0,58,44,0);
//        _formatoOriginal = 2;
//        estirar(new File(x), horaDesde, horaHasta);
//          guardarFormato2();
        /**********     Fin estirar             *********************/

        String f = "C:\\_grabar-ver\\sex and the city\\02\\E10 - The Caste System.srt";
        new Subtitulos(f);
        parsearFormato2();
//        offset(new Hora(-16, 0), new Hora(0));
        guardarFormato2();
//        s.formato1Aformato2();
        System.out.println("Listo");
    }



//    public static void estirar(File fileName,Hora horaDesde, Hora horaHasta) throws IOException {
//        _fileName = fileName.getPath();
//        _inicioS = horaDesde.todo;
//        _finS = horaHasta.todo;
//
//        parsear();
//        Subtitulo stI   = (Subtitulo) _subtitulos.elementAt(0);
//        Subtitulo stF   = (Subtitulo) _subtitulos.elementAt(_subtitulos.size() - 1);
//        long inicioO    = stI.getInicio().todo;
//        long finO       = stF.getInicio().todo;
//
//        float diferenciaO = finO - inicioO;
//        float diferenciaS = _finS - _inicioS;
//        float cons   = diferenciaS / diferenciaO;
//
//        //En este ciclo modifico la hora de cada uno de los subtítuos del vector.
//        for (int i = 0; i < _subtitulos.size();i++ ) {
//            Subtitulo sub   = (Subtitulo) _subtitulos.elementAt(i);
//            Hora hora = sub.getInicio();
//            float tem = ((float)(hora.todo - inicioO)) * cons;
//            long nuevo = ((long) tem) + _inicioS;
//            hora.set(nuevo);
//        }
////        guardar();
//    }

    //Formato = HH:MM:SS:texto
    public static Vector parsearFormato1() throws IOException{
        _subtitulos = new Vector();
        File file = new File(_fileName);
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        while(lnr.ready()){
            String linea = lnr.readLine();
            if (linea != null && linea.length() > 9){
                String h = linea.substring(0, 2);
                String m = linea.substring(3, 5);
                String s = linea.substring(6, 8);
                String t = linea.substring(9);
                Hora hora = new Hora(   Integer.parseInt(h),
                                        Integer.parseInt(m),
                                        Integer.parseInt(s),
                                        0);
                Subtitulo subt = new Subtitulo(hora, t);
                _subtitulos.add(subt);
            }
        }
        lnr.close();
        return _subtitulos;
    }

    public void formato3Aformato1() throws Exception {
        parsearFormato3();
        guardar();
    }
    public void formato2Aformato1() throws Exception {
        parsearFormato2();
        guardar();
    }

    /**
     * Del formato HH:MM:SS:texto
     * al formato srt
     *
     * 1
     * 00:00:00,167 --> 00:00:02,044
     * hazme una lista de
     * todo_ lo que quieres...
     *
     * 2
     * 00:00:02,127 --> 00:00:05,339
     * y planea los próximos
     * 25 años de tu vida obteniéndolo.
     *
     * @throws java.io.IOException
     */
    public void formato1Aformato2() throws IOException {
        parsearFormato1();
        guardarFormato2();
    }

    /**
     * Del formato sub:
     *
     * [END INFORMATION]
     * [SUBTITLE]
     * [COLF]&HFFFFFF,[STYLE]bd,[SIZE]18,[FONT]Arial
     * 00:00:36.50,00:00:38.90
     * "No cesaremos de explorar...
     *
     * 00:00:38.97,00:00:41.93
     * y el final de toda nuestra exploración[br]será llegar al punto de partida...
     *
     * al formato srt
     *
     * 1
     * 00:00:00,167 --> 00:00:02,044
     * hazme una lista de
     * todo_ lo que quieres...
     *
     * 2
     * 00:00:02,127 --> 00:00:05,339
     * y planea los próximos
     * 25 años de tu vida obteniéndolo.
     *
     * @throws java.io.IOException
     */
    public void formato4Aformato2() throws IOException {
        parsearFormato4();
        guardarFormato2();
    }

    /**
     * Del formato sub:
     *
     * [END INFORMATION]
     * [SUBTITLE]
     * [COLF]&HFFFFFF,[STYLE]bd,[SIZE]18,[FONT]Arial
     * 00:00:36.50,00:00:38.90
     * "No cesaremos de explorar...
     *
     * 00:00:38.97,00:00:41.93
     * y el final de toda nuestra exploración[br]será llegar al punto de partida...
     */
    private Vector parsearFormato4() {
        _subtitulos = new Vector();
        File file = new File(_fileName);
        LineNumberReader lnr = null;
        try{
            lnr = new LineNumberReader(new FileReader(file));
            String linea = "";
            while(lnr.ready()){
                linea = lnr.readLine();
                while (linea == null
                        || linea.trim().equals("")
                        || linea.trim().startsWith("[")) {
                    linea = lnr.readLine();
                }
                //la primer línea es: 00:00:36.50,00:00:38.90

                if (linea.trim().length() < 23){
                    System.out.println("problemas con la línea: " + linea);
                    return null;
                }
                Hora hora = null;
                Hora horaF = null;
                try{
                    String hd = linea.substring(0, 2);
                    String md = linea.substring(3, 5);
                    String sd = linea.substring(6, 8);
                    String msd = linea.substring(9, 11);

                    hora = new Hora(Integer.parseInt(hd),
                                         Integer.parseInt(md),
                                         Integer.parseInt(sd),
                                         Integer.parseInt(msd));

                    hd = linea.substring(12, 14);
                    md = linea.substring(15, 17);
                    sd = linea.substring(18, 20);
                    msd = linea.substring(21, 23);

                    horaF = new Hora(Integer.parseInt(hd),
                                         Integer.parseInt(md),
                                         Integer.parseInt(sd),
                                         Integer.parseInt(msd));

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("promemas con la línea: " + linea);
                }
                //El subtítulo ocupa solo una línea
                String t = "";
                linea = lnr.readLine();
                t = linea.replaceAll("\\[br\\]", SALTO_LINEA);

                Subtitulo subt = new Subtitulo(hora, horaF, t);
                _subtitulos.add(subt);
            }
            lnr.close();
            return _subtitulos;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return null;
        } finally{
            if (lnr != null)
                try {
                    lnr.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
    }


    public static void guardarFormato2() throws IOException {
        BufferedWriter bw = null;
        try{
            File file = new File(_fileName + "VP");
            FileOutputStream fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

            int cont = 1;
            for (int i = 0; i < _subtitulos.size(); i++) {
                Subtitulo s = (Subtitulo) _subtitulos.elementAt(i);
                Hora max = null;
                if (_subtitulos.size() > i + 1){
                    Subtitulo sTem = (Subtitulo) _subtitulos.elementAt(i + 1);
                    max = sTem.getInicio();
                }
                String sal = subtituloConFormato2(s, cont, max);
                sal += Constantes.NUEVA_LINEA;
                bw.write(sal);
                cont++;
            }
        }catch(IOException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            if (bw != null)
                bw.close();
        }
    }

    public static void buscarPalabras() throws IOException {
        for (int i = 0; i < _subtitulos.size(); i++) {
            Subtitulo s = (Subtitulo) _subtitulos.elementAt(i);
            if (contieneNumero(s.getTexto())){
                System.out.println("_fileName = " + _fileName);
                System.out.println("s.getTexto() = " + s.getTexto());
                System.out.println("");
            }
        }
    }

    private static boolean contieneNumero(String s){

        for(int i =1;i<10;i++){
            if (s.contains("" + i)){
                return true;
            }
        }
        return false;
    }

    public static void guardarFormatoSinTiempo() throws IOException {
        BufferedWriter bw = null;
        try{
            File file = new File(_fileName + ".txt");
            FileOutputStream fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < _subtitulos.size(); i++) {
                Subtitulo s = (Subtitulo) _subtitulos.elementAt(i);

                String sal = s.getTexto();
                sal += Constantes.NUEVA_LINEA;
                sal += Constantes.NUEVA_LINEA;
                bw.write(sal);
            }
        }catch(IOException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            if (bw != null)
                bw.close();
        }
    }

    /**
     * Salida:
     * 1
     * 00:00:00,167 --> 00:00:02,044
     * hazme una lista de
     * todo_ lo que quieres...
     *
     * 2
     * 00:00:02,127 --> 00:00:05,339
     * y planea los próximos
     * 25 años de tu vida obteniéndolo.
     *
     * @param sub
     * @param cont
     * @return
     */
    private static String subtituloConFormato2(Subtitulo sub, int cont, Hora max) {
        String res = cont + Constantes.NUEVA_LINEA;

        Hora hora = sub.getInicio();
//        if (hora.ms == 0){
//            hora.ms = (long)(Math.random() * 1000);
//        }
        String h = (hora.hora < 10) ? ("0" + hora.hora):("" + hora.hora);
        String m = (hora.min < 10) ? ("0" + hora.min):("" + hora.min);
        String s = (hora.seg < 10) ? ("0" + hora.seg):("" + hora.seg);
        String ms = (hora.ms < 100) ? ("0" + hora.ms):("" + hora.ms);
        if (ms.length() < 3){
            ms = "0" + ms;
        }
        res += h + ":" + m + ":" + s + "," + ms + " --> ";

        String txt = "";
        int cantLin = 0;
        StringTokenizer st = new StringTokenizer(sub.getTexto(), "|", false);
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            cantLin++;
            txt += t + Constantes.NUEVA_LINEA;
        }
        Hora fin = sub.getFin();
        if (fin == null){
            fin = (Hora)hora.clone();
            fin.add(_duracionSubs * cantLin);
        }
//        if (fin.ms == 0){
//            fin.ms = (long)(Math.random() * 1000);
//        }
        if (max != null && fin.esMayor(max)){
            fin = max;
        }

        h = (fin.hora < 10) ? ("0" + fin.hora):("" + fin.hora);
        m = (fin.min < 10) ? ("0" + fin.min):("" + fin.min);
        s = (fin.seg < 10) ? ("0" + fin.seg):("" + fin.seg);
        ms = (fin.ms < 100) ? ("0" + fin.ms):("" + fin.ms);
        if (ms.length() < 3){
            ms = "0" + ms;
        }
        res += h + ":" + m + ":" + s + "," + ms + Constantes.NUEVA_LINEA;
        res += txt;
        return res;
    }

    /**
     * Formato 3:
     *
     * 00:04:17.23,00:04:18.66
     * ¿Quien esta alla?
     *
     * 00:04:36.21,00:04:38.27
     * ¡Alto!
     */
    public static Vector parsearFormato3() throws IOException{
        _subtitulos = new Vector();
        File file = new File(_fileName);
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        String linea = "";
        while(lnr.ready()){
            linea = lnr.readLine();
            while (linea == null || linea.trim().equals("")) {
                linea = lnr.readLine();
            }
            //la primer línea es: 00:04:17.23,00:04:18.66
            //solo guargo la primera hora

            if (linea.trim().length() < 8){
                System.out.println("problemas con la línea: " + linea);
                return null;
            }
            Hora hora = null;
            try{
                String hd = linea.substring(0, 2);
                String md = linea.substring(3, 5);
                String sd = linea.substring(6, 8);
                String msd = linea.substring(9, 11);

                hora = new Hora(Integer.parseInt(hd),
                                     Integer.parseInt(md),
                                     Integer.parseInt(sd),
                                     Integer.parseInt(msd));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("promemas con la línea: " + linea);
            }
            //las siguientes líneas (hasta que haya una línea vacía, son los títlos)
            String t = "";
            linea = lnr.readLine();
            while (linea != null && !linea.trim().equals("")) {
                t += linea + SALTO_LINEA;
                linea = lnr.readLine();
            }
            //elimino el último SALTO_LINEA
            if (t.length() > 0){
                t = t.substring(0, t.length() - 1);
            }
            Subtitulo subt = new Subtitulo(hora, t);
            _subtitulos.add(subt);
        }
        lnr.close();
        return _subtitulos;
    }

/*****************************************************************************/
/*  Métodos privados                                                         */
/*****************************************************************************/

    private static void parsear() throws IOException{
        if (_formatoOriginal == 1)
            parsearFormato1();
        else if (_formatoOriginal == 2)
            parsearFormato2();
    }

    static long difMin = Long.MAX_VALUE;
    static long difMax = Long.MIN_VALUE;

    static long difMin2 = Long.MAX_VALUE;
    static long difMax2 = Long.MIN_VALUE;

//    Formato:
//    1
//    00:00:00,167 --> 00:00:02,044
//    hazme una lista de
//    todo_ lo que quieres...
//
//    2
//    00:00:02,127 --> 00:00:05,339
//    y planea los próximos
//    25 años de tu vida obteniéndolo.
//
//    3
//    .....
    public static void parsearFormato2() throws IOException{
        _subtitulos = new Vector();
        File file = new File(_fileName);
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        String linea = "";
        boolean prueba = true;
        Hora horaHastaAnterior = null;
        while(lnr.ready()){
            linea = lnr.readLine();
            while (linea == null || linea.trim().equals("")) {
                if(!lnr.ready()) return;
                linea = lnr.readLine();
            }
            //la primer línea es un contador. Ignoro esa línea.

            if(!lnr.ready()) return;

            //la segunda línea es el tiempo
            //00:00:00,167 --> 00:00:02,044
            linea = lnr.readLine();
            if (linea != null && linea.length() > 9){
                String hd = linea.substring(0, 2);
                String md = linea.substring(3, 5);
                String sd = linea.substring(6, 8);
                String msd = linea.substring(9, 12);
                Hora hora = new Hora(   Integer.parseInt(hd),
                                        Integer.parseInt(md),
                                        Integer.parseInt(sd),
                                        Integer.parseInt(msd));


                String hdF = linea.substring(17, 19);
                String mdF = linea.substring(20, 22);
                String sdF = linea.substring(23, 25);
                String msdF = linea.substring(26, 29);
                Hora horaF = new Hora(   Integer.parseInt(hdF),
                                        Integer.parseInt(mdF),
                                        Integer.parseInt(sdF),
                                        Integer.parseInt(msdF));

                //prueba
                if (hora.esMayor(horaF)){
                    System.out.println("Mal un horario.");
                    System.out.println("    hora desde = " + hora.toString());
                    System.out.println("    hora hasta = " + horaF.toString());
                    prueba = false;
                }
                long dif = horaF.diferenciaEnMiliseg(hora);
                if (dif > difMax)
                    difMax = dif;
                if (dif < difMin)
                    difMin = dif;
                ver(horaHastaAnterior, hora);
                horaHastaAnterior = hora;

                //las siguientes líneas (hasta que haya una línea vacía, son los títulos)
                String t = "";
                linea = lnr.readLine();
                while (linea != null && !linea.trim().equals("")) {
                    t += linea + SALTO_LINEA;
                    linea = lnr.readLine();
                }
                //elimino el último SALTO_LINEA
                if (t.length() > 0){
                    t = t.substring(0, t.length() - 1);
                }
                Subtitulo subt = new Subtitulo(hora, horaF,  t);
                _subtitulos.add(subt);
            }
        }
        lnr.close();
    }

    private static void ver(Hora horaHastaAnterior, Hora hora) {
        if (horaHastaAnterior == null){
            System.out.println("horaHastaAnterior == null [una vez está bien]");
            return ;
        }
        if (horaHastaAnterior.esMayor(hora)){
            System.out.println("horaHastaAnterior.esMayor(hora)");
            System.out.println("    horaHastaAnterior = " + horaHastaAnterior);
            System.out.println("    hora = " + hora);
        }

        long dif = hora.diferenciaEnMiliseg(horaHastaAnterior);
        if (dif > difMax2)
            difMax2 = dif;
        if (dif < difMin2)
            difMin2 = dif;
    }

    /**
     * Guarda en el archivo fileName los subtitulos que están en _subtitulos con
     * el formato indicado (en una variable global) como de salida.
     */
    private static void guardar() throws IOException{
        File file = new File(_fileName + "VP");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < _subtitulos.size();i++ ) {
            String sal = subtituloConFormato((Subtitulo)_subtitulos.elementAt(i));
            bw.write(sal);
            bw.newLine();
        }
        bw.close();
    }

    /**
     * A partir de un subtítulo devuelve el String con la hora y el texto en
     * el formato especificado como de salida (por una variable global).
     */
    private static String subtituloConFormato(Subtitulo sub){
        String res = "";
        if (_formatoSalida == 1){
            Hora hora = sub.getInicio();
            String h = (hora.hora < 10) ? ("0" + hora.hora):("" + hora.hora);
            String m = (hora.min < 10) ? ("0" + hora.min):("" + hora.min);
            String s = (hora.seg < 10) ? ("0" + hora.seg):("" + hora.seg);
            res = h + ":" + m + ":" + s + ":" + sub.getTexto();
        }
        return res;
    }

    private static Vector unir (File prim, File seg) throws Exception{
        LineNumberReader lnr1 = null;
        LineNumberReader lnr2 = null;
        String linea = null;
        String linea2 = null;
        try {


            _subtitulos = new Vector();
            lnr1 = new LineNumberReader(new FileReader(prim));
            lnr2 = new LineNumberReader(new FileReader(seg));

            while(lnr1.ready()){
                linea = lnr1.readLine();
                linea2 = lnr2.readLine();
                if (linea != null && linea.length() > 9){
                    String h = linea.substring(0, 2);
                    String m = linea.substring(3, 5);
                    String s = linea.substring(6, 8);
                    String t = linea.substring(9);
                    String t2 = linea2.substring(9);

                    Hora hora = new Hora(   Integer.parseInt(h),
                                            Integer.parseInt(m),
                                            Integer.parseInt(s),
                                            0);
                    t = t + SALTO_LINEA + t2;
                    Subtitulo subt = new Subtitulo(hora, t);
                    _subtitulos.add(subt);
                }
            }
        } catch (Exception ex) {
            System.out.println("Falló con esta líneas :" );
            System.out.println("    uno = " + linea);
            System.out.println("    dos = " + linea2);

        }
        finally{
            lnr1.close();
            lnr2.close();
        }
        return _subtitulos;
    }
}
