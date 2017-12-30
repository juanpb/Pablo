package p.aplic.subtitulos;

import p.util.Constantes;
import p.util.Hora;
import p.util.UtilFile;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Apr 1, 2008
 * Time: 1:36:54 PM
 * <p/>
 * <p/>
 * Formato 1: .sub
 * HH:MM:SS:texto
 * <p/>
 * <p/>
 * Formato 2: .srt
 * 1
 * 00:00:00,167 --> 00:00:02,044
 * hazme una lista de
 * todo_ lo que quieres...
 * 2
 * 00:00:02,127 --> 00:00:05,339
 * y planea los próximos
 * 25 años de tu vida obteniéndolo.
 */
public class Util {
    private static final String SALTO_LINEA = "|";
    private static final long duracionSubs = 2000L;


    public static List<Subtitulo> getSubtitulos(String arch) throws IOException {
        return getSubtitulos(new File(arch));
    }

    public static List<Subtitulo> getSubtitulos(File arch) throws IOException {
        List<Subtitulo> res;
        List<String> lineas = UtilFile.getArchivoPorLinea(arch);
        if (esFormato1(lineas)) {
            res = parsearFormato1(lineas);
        } else if (esFormatoSRT(lineas)) {
            res = parsearFormato2(lineas);
        } else {
            throw new RuntimeException("Formato no implementado para parsear.");
        }
        return res;
    }

    public static List<Subtitulo> eliminarSubsVacios(List<Subtitulo> subs) {
        List<Subtitulo> res = new ArrayList<Subtitulo>();
        for(Subtitulo sub : subs) {
            final String texto = sub.getTexto();
            if (texto != null && texto.trim().length() >0 ) {
                res.add(sub);
            }
        }
        return res;
    }

    /**
     * Valida que inicio < fin y que el fin de un subt. sea menor que el inicio
     * del siguiente.
     *
     * @param subs
     * @return
     */
    public boolean validar(List<Subtitulo> subs) {
        boolean res = true;
        Subtitulo subAnt = null;
        for(Subtitulo sub : subs) {
            Hora inicio = sub.getInicio();
            Hora fin = sub.getFin();
            if (!fin.esMayor(inicio)) {
                System.out.print("Problemas: hora fin no es mayor que hora desde en subt: ");
                System.out.println(sub.toString());
                res = false;
            } else if (subAnt != null) {
                Hora fin2 = subAnt.getFin();
                if (!inicio.esMayor(fin2)) {
                    System.out.print("Problemas: hora fin es mayor que hora desde " +
                            "del susbt. siguiente en subt: ");
                    System.out.println(sub.toString());
                }
            }
            subAnt = sub;
        }
        return res;
    }

    public static void guardarFormatoSRT(File file, List<Subtitulo> subs)
            throws IOException {
        BufferedWriter bw = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

            int cont = 1;
            for(Subtitulo s : subs) {
                String sal = subtituloConFormatoSRT(s, cont++);
                sal += Constantes.NUEVA_LINEA;
                bw.write(sal);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bw != null)
                bw.close();
        }
    }

    private static String subtituloConFormatoSRT(Subtitulo sub, int cont) {
        String res = cont + Constantes.NUEVA_LINEA;

        Hora hora = sub.getInicio();
        //agrego ceros (si hace falta) para que quede hh:mm:ss:mmm
        String hhh = getHoraConFormato(hora);
        res += hhh + " --> ";

        String txt = "";
        int cantLin = 0;
        StringTokenizer st = new StringTokenizer(sub.getTexto(), SALTO_LINEA, false);
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            cantLin++;
            txt += t + Constantes.NUEVA_LINEA;
        }
        Hora fin = sub.getFin();
        if (fin == null) {
            fin = (Hora) hora.clone();
            fin.add(duracionSubs * cantLin);
        }

        hhh = getHoraConFormato(fin);
        res += hhh + Constantes.NUEVA_LINEA;
        res += txt;
        return res;
    }

    private static String getHoraConFormato(Hora hora) {
        String h = (hora.hora < 10) ? ("0" + hora.hora) : ("" + hora.hora);
        String m = (hora.min < 10) ? ("0" + hora.min) : ("" + hora.min);
        String s = (hora.seg < 10) ? ("0" + hora.seg) : ("" + hora.seg);
        String ms = (hora.ms < 100) ? ("0" + hora.ms) : ("" + hora.ms);
        if (ms.length() < 3) {
            ms = "0" + ms;
        }
        return h + ":" + m + ":" + s + "," + ms;
    }

    //Formato = HH:MM:SS:texto
    private static List<Subtitulo> parsearFormato1(List<String> lineas) {
        List<Subtitulo> res = new ArrayList();
        for(String linea : lineas) {
            if (linea != null && linea.length() > 9) {
                String h = linea.substring(0, 2);
                String m = linea.substring(3, 5);
                String s = linea.substring(6, 8);
                String t = linea.substring(9);
                Hora hora = new Hora(Integer.parseInt(h),
                        Integer.parseInt(m),
                        Integer.parseInt(s),
                        0);
                Subtitulo subt = new Subtitulo(hora, t);
                res.add(subt);
            }
        }
        return res;
    }

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
    private static List<Subtitulo> parsearFormato2(List<String> lineas) {
        //saco las líneas vacías que estén al final
        boolean seguir = true;
        for(int i = lineas.size() - 1; i > 0 && seguir; i--) {
            String linea = lineas.get(i);
            if (linea == null || linea.trim().equals("")) {
                lineas.remove(i);
            } else
                seguir = false;
        }

        List<Subtitulo> res = new ArrayList();
        int cont = 0;
        int contSubt = 1;
        while (cont < lineas.size()) {
            String linea = lineas.get(cont++);

            //por si hay líneas vacías al principio o entre número y número
            while (linea == null || linea.trim().equals("")) {
                linea = lineas.get(cont++);
            }

            //la primer línea es un contador. Ignoro esa línea.
            try {
                if (new Integer(linea.trim()) != contSubt) {
                    String msj = "Problemas con el contador. Leído: " +
                            linea + ", esperado: " + contSubt;
                    System.out.println(msj);
                }
            } catch (Exception e) {
                String msj = e.getMessage() + " Problemas con la línea: " + linea + ". Se esperaba un int.";
                JOptionPane.showMessageDialog(null, msj);
                e.printStackTrace();
            }
            contSubt++;

            //la segunda línea es el tiempo
            //00:00:00,167 --> 00:00:02,044
            linea = lineas.get(cont++);

            try {
                if (linea != null && linea.length() > 9) {
                    String hd = linea.substring(0, 2);
                    String md = linea.substring(3, 5);
                    String sd = linea.substring(6, 8);
                    String msd = linea.substring(9, 12);
                    Hora hora = new Hora(Integer.parseInt(hd),
                            Integer.parseInt(md),
                            Integer.parseInt(sd),
                            Integer.parseInt(msd));


                    String hdF = linea.substring(17, 19);
                    String mdF = linea.substring(20, 22);
                    String sdF = linea.substring(23, 25);
                    String msdF = linea.substring(26, 29);
                    Hora horaF = new Hora(Integer.parseInt(hdF),
                            Integer.parseInt(mdF),
                            Integer.parseInt(sdF),
                            Integer.parseInt(msdF));

                    //las siguientes líneas, hasta que haya una línea vacía,
                    //son los títulos
                    String t = "";
                    linea = lineas.get(cont++);
                    while (linea != null && !linea.trim().equals("")) {
                        t += linea + SALTO_LINEA;
                        if (cont < lineas.size())
                            linea = lineas.get(cont++);
                        else
                            linea = null;
                    }
                    //elimino el último SALTO_LINEA
                    if (t.length() > 0) {
                        t = t.substring(0, t.length() - 1);
                    }
                    Subtitulo subt = new Subtitulo(hora, horaF, t);
                    res.add(subt);
                }
            } catch (Exception e) {
                String msj = e.getMessage() + " Problemas con la línea: " + linea + ". Se esperaba HH:MM:SS,MIL --> HH:MM:SS,MIL";
                JOptionPane.showMessageDialog(null, msj);
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * Asumo que es Formato 1 si la primera línea es de la forma: "HH:MM:SS:..."
     *
     * @param lineas
     * @return
     */
    private static boolean esFormato1(List<String> lineas) {
        if (lineas.size() > 0) {
            String l = lineas.get(0).trim();
            return (l.length() > 8 &&
                    l.charAt(2) == ':' &&
                    l.charAt(5) == ':' &&
                    l.charAt(8) == ':');
        }
        return false;
    }

    /**
     * Asumo que es Formato 1 si la primera línea es = a "1"
     *
     * @param f
     * @return
     */
    public static boolean esFormatoSRT(File f) throws IOException {
        return esFormatoSRT(UtilFile.getArchivoPorLinea(f));
    }

    /**
     * Asumo que es Formato 1 si la primera línea es = a un número y la segunda tiene la forma 00:00:44,625 --> 00:00:48,685
     *
     * @param lineas
     * @return
     */
    public static boolean esFormatoSRT(List<String> lineas) {
        if (lineas.size() > 1) {
            String l = lineas.get(0).trim();
            try {
                Integer.getInteger(l);
            } catch (Exception e) {
                return false;
            }
            l = lineas.get(1).trim();
            int ind = l.indexOf(" --> ");
            return ind == 12;
        }
        return false;
    }

    public static List<Subtitulo> estirar(File file, Hora horaDesde, Hora horaHasta)
            throws IOException {
        long inicioNuevo = horaDesde.todo;
        long finNuevo = horaHasta.todo;


        List<Subtitulo> subs = getSubtitulos(file);
        Subtitulo stI = subs.get(0);
        Subtitulo stF = subs.get(subs.size() - 1);
        long inicioOrig = stI.getInicio().todo;
        long finOrig = stF.getInicio().todo;

        float diferenciaO = finOrig - inicioOrig;
        float diferenciaS = finNuevo - inicioNuevo;
        double cons = diferenciaS / diferenciaO;

        //En este ciclo modifico la hora de cada uno de los subtítuos
        for(Subtitulo sub : subs) {
            long duración = sub.getDuracion();
            Hora hora = sub.getInicio();
            double tem = ((double) (hora.todo - inicioOrig)) * cons;
            long nuevoInicio = ((long) tem) + inicioNuevo;
            hora.set(nuevoInicio);
            sub.getFin().set(nuevoInicio + duración);
        }
        ajustarInicioFin(subs);
        return subs;
    }

    /**
     * Modifica la duración (la hora de fin) de un subtítulo si se pisa con el siguiente subtítulo
     *
     * @param subs
     */
    private static void ajustarInicioFin(List<Subtitulo> subs) {
        int ind = 1;
        for(Subtitulo sub : subs) {
            if (ind < subs.size()) {
                Subtitulo proxS = subs.get(ind++);
                Hora fin = sub.getFin();
                if (fin.esMayor(proxS.getInicio())) {
                    sub.setFin(proxS.getInicio());
                }
            }
        }
    }

    /**
     * Agrega un offset a todos los subtítulos posteriores a  horaDesde
     *
     * @param offset
     * @param horaDesde
     */
    public static void offset(Hora offset,
                              Hora horaDesde, List<Subtitulo> subs, boolean sumar) {
        for(Object _subtitulo : subs) {
            Subtitulo sub = (Subtitulo) _subtitulo;
            Hora h = sub.getInicio();


            if (horaDesde == null || h.esMayor(horaDesde)) {
                if (sumar)
                    h.add(offset);
                else
                    h.restar(offset.todo);

                Hora horaFin = sub.getFin();
                if (horaFin != null)
                    if (sumar)
                        horaFin.add(offset);
                    else
                        horaFin.restar(offset.todo);
            }
        }
    }

    public static void offset(int hora, int min, int seg, int ms, List<Subtitulo> subs, boolean sumar) {
        offset(new Hora(hora, min, seg, ms), null, subs, sumar);
    }

    public static List<String> getTexto(List<Subtitulo> subs) throws Exception {
        List<String> res = new ArrayList<String>(subs.size());
        for(Subtitulo s : subs) {
            res.add(s.getTexto());
        }
        return res;
    }

    public static StringBuffer getTextoAsString(List<Subtitulo> subs) throws Exception {
        StringBuffer res = new StringBuffer();
        for(Subtitulo s : subs) {
            res.append(s.getTexto()).append(Constantes.NUEVA_LINEA);
        }
        return res;
    }


    public static void imprimirTexto(List<Subtitulo> subs) throws Exception {
        for(Subtitulo s : subs) {
            System.out.println(s.getTexto());
        }
    }

    public static void reemplazarSimboloParaItalic(List<Subtitulo> subs)
            throws Exception {
        String simb = "\"";
        int cont= 0;
        for(Subtitulo s : subs) {
            final String texto = s.getTexto();
            if(texto.startsWith(simb) && texto.endsWith(simb) ){
                cont++;
                s.setTexto(
                        "<i>" + texto.replaceAll(simb, "") + "</i>");
            }
        }
        System.out.println("Se reemplazaron: " + cont + " líneas.");
    }

    //solo agrega '¿' o '¡' "en la misma línea"
    public static List<Subtitulo> cerrarSignosAdmYPreg(List<Subtitulo> subs)
            throws Exception {
        List<Subtitulo> res = new ArrayList<Subtitulo>(subs.size());
        int indice = 0;
        for(Subtitulo s : subs) {
            String texto = s.getTexto();
            texto = agregarSignoSiCorresponde(texto);
            res.add( new Subtitulo(s.getInicio(), s.getFin(), texto)) ;
            indice++;
        }
        return res;
    }

    private static String agregarSignoSiCorresponde(String x){
        if (x.indexOf("?")>0){
            int i = x.substring(0, x.indexOf("?")).lastIndexOf(".");
//            if (i > 0)
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        String dir = "Z:\\videos HD - Musicales\\Woodstock - Ultimate Collectors Edition. Includes Directors Cut Bonus\\Woodstock. 3 Days of Peace & Music. The Direcor's Cut";
        String arch = "Woodstock. 3 Days of Peace & Music.srt";
        String x = dir + "\\" + arch;
        List<Subtitulo> subs = getSubtitulos(x);
        reemplazarSimboloParaItalic(subs);
        guardarFormatoSRT(new File(dir, arch+"2"), subs);
//        imprimirTexto(subs);
//        s.formato1Aformato2();
//        Hora horaDesde = new Hora(0,0,4,0);
//        Hora horaHasta = new Hora(0,53,0,0);
//        _formatoOriginal = 2;
//        estirar(new File(x), horaDesde, horaHasta);
//          guardarFormato2();
        /****************offset*****************/
//        offset(0, 0, -8, -387, subs);
//        guardarFormatoSRT(new File(dir, arch+"2"), subs);
        /****************offset*****************/
        /**********      Estirar                *********************/
//        Hora horaDesde = new Hora(0, 1, 19, 0); //
//        Hora horaHasta = new Hora(1, 15, 11, 500);  ////0.0.2
//
//
//        File oldFile = new File(x);
//        subs = estirar(oldFile, horaDesde, horaHasta);
//        //renombro viejo:
//        oldFile.renameTo(new File(x + ".old"));

//         guardarFormatoSRT(new File(dir, arch+"2"), subs);
//        File newFile = new File(x);
//        if (!newFile.exists()) {
//            guardarFormatoSRT(newFile, subs);
//        } else
//            System.out.println("Error.");

        /**********     Fin estirar             *********************/


        System.out.println("Listo");
    }


    /**
     * Modifica la duración (la hora de fin) de un subtítulo si se pisa con el siguiente subtítulo
     *
     * @param subs
     */
    public static void cortarHasta(List<Subtitulo> subs) {
        ajustarInicioFin(subs);
    }

    public static List<Subtitulo> unir(List<Subtitulo> subs1, List<Subtitulo> subs2) {
        List<Subtitulo> res = new ArrayList<Subtitulo>();
        int indice1 = 0;
        int indice2 = 0;
        Subtitulo sub1 = subs1.get(indice1++);
        Subtitulo sub2 = subs2.get(indice2++);

        while(true){
            if (sub1.esAnteriorA(sub2)){
                res.add(sub1);
                if (subs1.size() == indice1){
                    res.add(sub2);
                    res.addAll(subs2.subList(indice2, subs2.size()));
                    break;
                }

                sub1 = subs1.get(indice1++);
            }else{
                res.add(sub2);
                if (subs2.size() == indice2){
                    res.add(sub1);
                    res.addAll(subs1.subList(indice1, subs1.size()));
                    break;
                }
                sub2 = subs2.get(indice2++);
            }
        }
        ajustarInicioFin(res);
        return res;
    }
}
