package p.proc.pplan;

import p.util.Fechas;

import java.util.Date;

/**
 * User: JPB
 * Date: 20/04/13
 * Time: 23:53
 */
public class RegImed extends Reg{


    protected static final int CDRSEC_DESDE = 3;
    protected static final int CDRSEC_HASTA = 11;
    protected static final int FECHA_DESDE = 11;
    protected static final int FECHA_HASTA = 25;
    protected static final int NUMERO_ORIGEN_DESDE = 75;
    protected static final int NUMERO_ORIGEN_HASTA = 103;
    protected static final int NUMERO_DEST_DESDE = 119;
    protected static final int NUMERO_DEST_HASTA = 147;
    protected static final int TIPO_DESDE = 40;
    protected static final int TIPO_HASTA = 41;
    protected static final int LOTE_DESDE = 41;
    protected static final int LOTE_HASTA = 49;
    protected static final int CDRTYPE_DESDE = 71;
    protected static final int CDRTYPE_HASTA = 74;
    protected static final int DURA_A_DESDE = 147;
    protected static final int DURA_A_HASTA = 153;
    protected static final int DURA_T_DESDE = 153;
    protected static final int DURA_T_HASTA = 159;
    public static final int CHARG_DUR_DESDE = 147; //148-153:	ChargeableDuration
    public static final int CHARG_DUR_HASTA = 153;
    public static final int CON_TIME_DESDE = 153; //154-159:	ConnectionTime
    public static final int CON_TIME_HASTA = 159;

    protected static final int REDIR_NUM_DESDE = 192;
    protected static final int REDIR_NUM_HASTA = 212;

    public static final int ORIGIN_AREA_DESDE = 246;
    public static final int ORIGIN_AREA_HASTA = 253;
    public static final int DESTINATION_AREA_DESDE = 253;
    public static final int DESTINATION_AREA_HASTA = 260;

    public static final int ADIT_INFO_1_DESDE = 263;
    public static final int ADIT_INFO_1_HASTA = 268;
    public static final int ADIT_INFO_2_DESDE = 268;
    public static final int ADIT_INFO_2_HASTA = 273;


    public RegImed(String linea) {
        super(linea);
    }


    @Override
    public int getFechaDesde() {
        return FECHA_DESDE;
    }

    @Override
    public int getFechaHasta() {
        return FECHA_HASTA;
    }

    @Override
    public int getNumeroOrigDesde() {
        return NUMERO_ORIGEN_DESDE;
    }

    @Override
    public int getNumeroOrigHasta() {
        return NUMERO_ORIGEN_HASTA;
    }

    @Override
    public int getNumeroDestDesde() {
        return NUMERO_DEST_DESDE;
    }

    @Override
    public int getNumeroDestHasta() {
        return NUMERO_DEST_HASTA;
    }

    @Override
    public int getTipoDesde() {
        return TIPO_DESDE;
    }

    @Override
    public int getTipoHasta() {
        return TIPO_HASTA;
    }

    @Override
    public int getDuraADesde() {
        return DURA_A_DESDE;
    }

    @Override
    public int getDuraAHasta() {
        return DURA_A_HASTA;
    }

    @Override
    public int getDuraTDesde() {
        return DURA_T_DESDE;
    }

    @Override
    public int getDuraTHasta() {
        return DURA_T_HASTA;
    }

    public static Date getHoraLlamada(String s) {
        s = s.substring(FECHA_DESDE, FECHA_HASTA);
        return Fechas.getDateAAAAMMDDHHMMSS(s);
    }

    public static String getTipoLlamada(String s) {
        return s.substring(TIPO_DESDE, TIPO_HASTA);
    }

    public String getOriginArea() {
        return linea.substring(ORIGIN_AREA_DESDE, ORIGIN_AREA_HASTA);
    }

    public String getDestinationArea() {
        return linea.substring(DESTINATION_AREA_DESDE, DESTINATION_AREA_HASTA);
    }

    public String getAditInfo1() {
        return linea.substring(ADIT_INFO_1_DESDE, ADIT_INFO_1_HASTA);
    }

    public String getAditInfo2() {
        return linea.substring(ADIT_INFO_2_DESDE, ADIT_INFO_2_HASTA);
    }


    public static String getNroOrigen(String s) {
        return s.substring(NUMERO_ORIGEN_DESDE, NUMERO_ORIGEN_HASTA);
    }

    public static String getCDRType(String s) {
        return s.substring(CDRTYPE_DESDE, CDRTYPE_HASTA);
    }

    public static String getCDRSec(String s) {
        return s.substring(CDRSEC_DESDE, CDRSEC_HASTA);
    }

    public static String getRedirNum(String s) {
        return s.substring(REDIR_NUM_DESDE, REDIR_NUM_HASTA);
    }
    public static String getTipo(String s) {
        return s.substring(TIPO_DESDE, TIPO_HASTA);
    }

    /**
     *
     * @return en segundos la suma de connection time y chargable duration
     */
    public static Long getValorDuracionAireEnSegundos(String s) {
        String x = s.substring(RegImed.CON_TIME_DESDE, RegImed.CON_TIME_HASTA);
        String y = s.substring(RegImed.CHARG_DUR_DESDE, RegImed.CHARG_DUR_HASTA);
        try {
            final long a = hhmmss2ss(x);
            final long b = hhmmss2ss(y);
            return a+b;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }
}
