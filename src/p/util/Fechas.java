package p.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fechas {

    private static Calendar _cal = null;
    private static long MILISEGUNDOS_POR_DIA = 1000 * 60 * 60 * 24;

    public Fechas() {
    }

    public static int getAnnusActual(){
        if (_cal == null)
            _cal = Calendar.getInstance();
        return getAnnus(_cal);
    }
    public static int getAnnus(Calendar pCal){
        return pCal.get(Calendar.YEAR);
    }

    public static int getMesActual(){
        if (_cal == null)
            _cal = Calendar.getInstance();
        return getMes(_cal);
    }
    public static int getMes(Calendar pCal){
        return pCal.get(Calendar.MONTH) + 1;
    }

    public static int getDiaActual(){
        if (_cal == null)
            _cal = Calendar.getInstance();
        return getDia(_cal);
    }
    public static int getDia(Calendar pCal){
        return pCal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Devuelve el número de año en formato String
     */
    public static String getAnnusString(Calendar pCal){
        return Integer.toString(pCal.get(Calendar.YEAR));
    }

    /**
     * Devuelve el número de año en formato String
     */
    public static String getAnnusString(){
        if (_cal == null)
            _cal = Calendar.getInstance();
        return getAnnusString(_cal);
    }

    /**
     * Devuelve el número de mes en formato String
     */
    public static String getMesString(Calendar pCal){
        String res = Integer.toString(1 + pCal.get(Calendar.MONTH));
        if (res.length() == 1)
            res = "0" + res;
        return res;
    }

    /**
     * Devuelve el número de mes en formato String
     */
    public static String getMesString(){
        if (_cal == null)
            _cal = Calendar.getInstance();
        return getMesString(_cal);
    }

    /**
     * Devuelve el número de día en formato String
     */
    public static String getDiaString(Calendar pCal){
        String res = Integer.toString(pCal.get(Calendar.DAY_OF_MONTH));
        if (res.length() == 1)
            res = "0" + res;
        return res;
    }

    /**
     * Devuelve el número de día en formato String
     */
    public static String getDiaString(){
        if (_cal == null)
            _cal = Calendar.getInstance();
        return getDiaString(_cal);
    }



    /**
     * Devuelve un String (con el formato: HH:MM:SS), con el promedio.
     */
    public static String promedio(int pHor,int pMin, int pSeg, int pDias){
        int s = (pHor * 3600 + pMin * 60 + pSeg)/pDias;
        int h = s / 3600;
        s = s % 3600;
        int m = s / 60;
        s = s % 60;
        return getHHMMSS(h, m, s);
    }

    public static String getNombreDia(int diaSem){
        switch( diaSem ) {
            case Calendar.MONDAY:
                return "Lunes";
            case Calendar.TUESDAY:
                return "Martes";
            case Calendar.WEDNESDAY:
                return "Miércoles";
            case Calendar.THURSDAY:
                return "Jueves";
            case Calendar.FRIDAY:
                return "Viernes";
            case Calendar.SATURDAY:
                return "Sábado";
            case Calendar.SUNDAY:
                return "Domingo";
            default:
                return "error";
            }
    }

    /**
     * Devuelve un String con la hora, con el formato
     * HH:MM:SS (siempre con dos dígitos cada parte)
     */
    public static String getHHMMSS(int hor, int min, int seg){
        return (hor < 10 ? "0" + hor : ""+hor ) + ":" +
               (min < 10 ? "0" + min : ""+min ) + ":" +
               (seg < 10 ? "0" + seg : ""+seg);
    }

    /**
     * Devuelve x hs, y min, z seg, m ms
     * @param miliseg
     * @return
     */
    public static String getTiempo(long miliseg){
        int ms = (int) (miliseg % 1000);
        int seg = new Long(miliseg / 1000).intValue();
        int horas = seg/3600;
        int minutos = (seg - horas * 3600)/60;
        int segundos = (seg - horas * 3600) - minutos * 60 ;
        String res = "";
        if (horas > 0)
            res += horas + " hs, ";
        if (minutos > 0)
            res += minutos + " min, ";
        if (segundos > 0)
            res += segundos + " seg, ";
        if (ms > 0)
            res += ms + " ms, ";
        if (res.endsWith(", "))
            res = res.substring(0, res.length() - 2) ;
        return res;
    }
    public static String getHHMMSS(long miliseg){
        int seg = new Long(miliseg / 1000).intValue();
        int horas = seg/3600;
        int minutos = (seg - horas * 3600)/60;
        int segundos = (seg - horas * 3600) - minutos * 60 ;
        return getHHMMSS(horas, minutos, segundos);
    }

    public static String getHHMMSS(Calendar cal){
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        return getHHMMSS(h, m, s);
    }

    /**
     * Calcula los días que separan dos fechas. Excluye ambos límites.
     * Ej: getDaysBetween("19-Feb-2002","28-Feb-2002") = 9
     */
    public static int getDaysBetween(java.util.Date pD1, java.util.Date pD2){
        long l1 = pD1.getTime();
        long l2 = pD2.getTime();
        return getDaysBetween(l1,l2);
    }

    /**
     * Calcula los días que separan dos fechas. Excluye ambos límites.
     * Ej: getDaysBetween("19-Feb-2002","28-Feb-2002") = 9
     */
    public static int getDaysBetween(long pL1, long pL2){
        return (int)((pL2-pL1)/MILISEGUNDOS_POR_DIA);
    }

    /**
     * suma/Resta la cantidad de días a la fecha.
     */
    public static java.util.Date addDays(java.util.Date pDate, int cantDias) {
        return new java.util.Date(addDays(pDate.getTime(),cantDias));
    }

    /**
     * suma/Resta la cantidad de días a la fecha.
     */
    public static long addDays(long fechaOrigen, int cantDias){
        return (fechaOrigen + MILISEGUNDOS_POR_DIA * cantDias);
    }


    public static String getFecha(Calendar cal){
        return getFecha(cal, "/");
    }

    /**
     * Devuelve numDia + separador + mes + separador + año (con dos dígitos para
     * numDia y mes)
     */
    public static String getFecha(Calendar cal, String separador){
        String año = Integer.toString(cal.get(Calendar.YEAR));
        String mes = Integer.toString(cal.get(Calendar.MONTH) + 1); //Porque devuelve un número menos
        String numDia = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (mes.length() == 1)
            mes = "0" + mes;
        if (numDia.length() == 1)
            numDia = "0" + numDia;

        return numDia + separador + mes + separador + año;
    }

    /**
     * Ojo: los meses van de 0..11
     */
    public static long getMilisegundos(int pAnnus, int pMes, int pDia,
                                       int pHora, int pMinutos){
        return getMilisegundos(pAnnus, pMes, pDia, pHora, pMinutos, 0);
    }

    /**
     * Ojo: los meses van de 0..11
     */
    public static long getMilisegundos(int pAnnus, int pMes, int pDia,
                                       int pHora, int pMinutos, int pSeg){
        if (_cal == null)
            _cal = Calendar.getInstance();
        _cal.set(pAnnus, pMes, pDia, pHora, pMinutos, pSeg);
        return _cal.getTime().getTime();
    }

    public static String getAAAADDMMHHMMSS(String sep){
        return getAAAADDMMHHMMSS(new Date().getTime(), sep);
    }

    public static String getAAAADDMMHHMMSS(){
        return  getAAAADDMMHHMMSS(new Date().getTime(), null);
    }

    /**
     *
     * @param x de la forma 2008-12-31
     * @return
     */
    public static Date getDateAAAA_MM_DD(String x){
        if (x == null || x.length() < 10)
            return null;
        String a = x.substring(0, 4);
        String m = x.substring(5, 7);
        String d = x.substring(8, 10);

        Calendar cal = Calendar.getInstance();
        int mes = Integer.parseInt(m);
        int dia = Integer.parseInt(d);
        if (mes < 1 || mes > 12 || dia < 1 || dia > 31)
            return null;

        cal.set(Integer.parseInt(a), mes -1 , dia);
        return cal.getTime();
    }

    public static String DD_MM_AAAA_2_AAAA_MM_DD(String x){
        if (x == null || x.length() < 10)
            return "";
        String sep = x.substring(2,3);
        String d = x.substring(0, 2);
        String m = x.substring(3, 5);
        String a = x.substring(6, 10);

        return a + sep + m + sep + d;
    }

    /**
     *
     * @param x de la forma AAAAMMDDHHMMSS
     * @return
     */
    public static Date getDateAAAAMMDDHHMMSS(String x){
        if (x == null || x.length() < 14)
            return null;
        String a = x.substring(0, 4);
        String m = x.substring(4, 6);
        String d = x.substring(6, 8);
        String h = x.substring(8, 10);
        String mi = x.substring(10, 12);
        String s = x.substring(12, 14);

        Calendar cal = Calendar.getInstance();
        int mes = Integer.parseInt(m);
        int dia = Integer.parseInt(d);
        if (mes < 1 || mes > 12 || dia < 1 || dia > 31)
            return null;

        cal.set(Integer.parseInt(a), mes -1 , dia, Integer.parseInt(h), Integer.parseInt(mi), Integer.parseInt(s));
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     *
     * @param x de la forma 08-12-31
     * @return
     */
    public static Date getDateDD_MM_AA(String x){
        if (x == null || x.length() < 8)
            return null;

        String d = x.substring(0, 2);
        String m = x.substring(3, 5);
        String a = x.substring(6, 8);
        a = "20" + a;
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(a), Integer.parseInt(m)-1 , Integer.parseInt(d));
        return cal.getTime();
    }

    public static String getAAAADDMMHHMMSS(long pMilis, String sep){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(pMilis));

        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(1+cal.get(Calendar.MONTH));
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String  h   = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String  m   = Integer.toString(cal.get(Calendar.MINUTE));
        String  s   = Integer.toString(cal.get(Calendar.SECOND));


        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        if (h.length() == 1)
            h = "0" + h;
        if (m.length() == 1)
            m = "0" + m;
        if (s.length() == 1)
            s = "0" + s;
        if (sep == null)
            return a + "-" + mes + "-" + d + " " + h + ":" + m + ":" + s;
        else
            return a + sep + mes + sep + d + sep + h + sep + m + sep + s;
    }

    //devuelve 15:45:50 17-10
    public static String getHHMMSSDDMMAAAA(long pMilis){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(pMilis));

        String  a = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(1+cal.get(Calendar.MONTH));
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String  h   = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String  m   = Integer.toString(cal.get(Calendar.MINUTE));
        String  s   = Integer.toString(cal.get(Calendar.SECOND));


        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        if (h.length() == 1)
            h = "0" + h;
        if (m.length() == 1)
            m = "0" + m;
        if (s.length() == 1)
            s = "0" + s;

        return h + ":" + m + ":" + s + " " + d + "-" + mes + "-" + a;
    }

    public static String getDDMMAAAAHHMMSS(){
        Calendar cal = Calendar.getInstance();
        return getDDMMAAAAHHMMSS(cal);
    }

    public static String getDDMMAAAAHHMMSS(long ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ms);
        return getDDMMAAAAHHMMSS(cal);
    }

    public static String getDDMMAAAAHHMMSS(Calendar cal){
        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(1+cal.get(Calendar.MONTH));
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String  h   = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String  m   = Integer.toString(cal.get(Calendar.MINUTE));
        String  s   = Integer.toString(cal.get(Calendar.SECOND));


        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        if (h.length() == 1)
            h = "0" + h;
        if (m.length() == 1)
            m = "0" + m;
        if (s.length() == 1)
            s = "0" + s;

        String sep = "/";
        return d + sep + mes + sep + a + " " + h + ":" + m + ":" + s;
    }

    public static String getAAAAMMDDHHMMSS(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }


    public static String getAAAAMMDDHHMMSS(String separador){
        Calendar cal = Calendar.getInstance();

        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(1+cal.get(Calendar.MONTH));
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String  h   = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String  m   = Integer.toString(cal.get(Calendar.MINUTE));
        String  s   = Integer.toString(cal.get(Calendar.SECOND));


        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        if (h.length() == 1)
            h = "0" + h;
        if (m.length() == 1)
            m = "0" + m;
        if (s.length() == 1)
            s = "0" + s;

        return a + separador + mes + separador + d + separador + separador + h + separador + m + separador + s;
    }

    public static String getAAAAMMDD_HHMMSS(){
        Calendar cal = Calendar.getInstance();

        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(1+cal.get(Calendar.MONTH));
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String  h   = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String  m   = Integer.toString(cal.get(Calendar.MINUTE));
        String  s   = Integer.toString(cal.get(Calendar.SECOND));


        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        if (h.length() == 1)
            h = "0" + h;
        if (m.length() == 1)
            m = "0" + m;
        if (s.length() == 1)
            s = "0" + s;

        return a  + mes + d + "_" + h + m + s;
    }

    public static String getAAAADDMM(){
        Calendar cal = Calendar.getInstance();

        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        return a + "-" + mes + "-" + d;
    }

    public static String getAAAAMMDD(String separador){
        Calendar cal = Calendar.getInstance();

        return getAAAAMMDD(separador, cal);
    }

    public static String getDDMMAA(String separador, long date){
        return getDDMMAA(separador, date, false);
    }

    public static String getDDMMAA(String separador, long date, boolean sacarCeros){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return getDDMMAA(separador, cal, sacarCeros);      
    }

    public static String getAAAAMMDD(String separador, long date){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return getAAAAMMDD(separador, cal);
    }

    private static String getDDMMAA(String separador, Calendar cal){
        return getDDMMAA(separador,  cal, false);
    }

    private static String getDDMMAA(String separador, Calendar cal,
                                    boolean sacarCeros){
        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        a = a.substring(2);
        if (sacarCeros && a.startsWith("0"))
            a = a.substring(1);

        String  mes = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

        if (mes.length() == 1 && !sacarCeros)
            mes = "0" + mes;
        if (d.length() == 1 && !sacarCeros)
            d = "0" + d;
        return d + separador + mes + separador + a;
    }

    private static String getAAAAMMDD(String separador, Calendar cal){
        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        return a + separador + mes + separador + d;
    }

    public static String getAAAAMMDD(int masDias,
                                     String separador){
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DATE, masDias);

        String  a   = Integer.toString(cal.get(Calendar.YEAR));
        String  mes = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String  d   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

        if (mes.length() == 1)
            mes = "0" + mes;
        if (d.length() == 1)
            d = "0" + d;
        return a + separador + mes + separador + d;
    }

    public static String getAAAAMMDDDiaSemana(int masDias, String separador){
        String res = getAAAAMMDD(masDias, separador);
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DATE, masDias);
        int ds = cal.get(Calendar.DAY_OF_WEEK);
        return res + " (" + getNombreDia(ds) + ")";
    }

    public static void main(String[] args){
        //sumar días a una fecha
//        Date d = getDateDD_MM_AA("26-07-11");
//        System.out.println("d = " + d);
//        Date date = addDays(d, 60);
//        System.out.println("date = " + date);
        System.out.println("getAAAAMMDDHHMMSS() = " + getAAAAMMDDHHMMSS("/"));
    }


}