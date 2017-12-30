package p.proc.pplan;

/**
 * User: JPB
 * Date: 20/04/13
 * Time: 23:53
 */
public abstract class Reg {
    protected static final String TIPO_ENTRANTE = "E";
    protected static final String TIPO_SALIENTE = "S";
    protected static final String TIPO_TRANSFERENCIA = "T";
    static final String TIPO_IMED = "I";
    static final String TIPO_PP = "P";
    static final String TIPO_PP_OUT = "O";

    protected String linea;

    /*
    protected static int FECHA_DESDE = 0;
    protected static int FECHA_HASTA = 0;
    protected static int NUMERO_ORIGEN_DESDE = 0;
    protected static int NUMERO_ORIGEN_HASTA = 0;
    protected static int TIPO_DESDE = 0;
    protected static int TIPO_HASTA = 0;
    */
    private static final String VALOR_HEADER = "01";
    private static final String VALOR_FOOTER = "99";

    public Reg(String linea) {
        this.linea = linea;
    }

    public boolean isFooter(){
        return linea.startsWith(VALOR_FOOTER);
    }

    public boolean isHeader(){
        return linea.startsWith(VALOR_HEADER);
    }

    public abstract int getFechaDesde();
    public abstract int getFechaHasta();
    public abstract int getNumeroOrigDesde();
    public abstract int getNumeroOrigHasta();
    public abstract int getNumeroDestDesde();
    public abstract int getNumeroDestHasta();
    public abstract int getTipoDesde();
    public abstract int getTipoHasta();
    public abstract int getDuraADesde();
    public abstract int getDuraAHasta();
    public abstract int getDuraTDesde();
    public abstract int getDuraTHasta();

    public Long getFecha() {
        String x = linea.substring(getFechaDesde(), getFechaHasta());
        try {
            return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    public String getNumOrigen() {
        String x = linea.substring(getNumeroOrigDesde(), getNumeroOrigHasta());
        return x.trim();
    }


    public String getNumDestino() {
        String x = linea.substring(getNumeroDestDesde(), getNumeroDestHasta());
        return x.trim();
    }

    //duración aire y ChargeableDuration
    public Long getDuracion1() {
        String x = linea.substring(getDuraADesde(), getDuraAHasta());
        try {
            if (this instanceof RegImed)
                return hhmmss2ss(x.trim());
            else
                return Long.parseLong(x.trim());

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    //duración tierra y ConnectionTime
    public Long getDuracion2() {
        String x = linea.substring(getDuraTDesde(), getDuraTHasta());
        try {
            if (this instanceof RegImed)
                return hhmmss2ss(x.trim());
            else
                return Long.parseLong(x.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0L;
        }
    }

    /**
     * Devuelve en segundos, el parametro recibido con formato HHMMSS
     * @param x
     * @return
     */
    public static long hhmmss2ss(String x){
        long h = Long.parseLong(x.substring(0,2)) ;
        long m = Long.parseLong(x.substring(2, 4)) ;
        long s = Long.parseLong(x.substring(4, 6)) ;

        m += h * 60;
        s += m * 60;
        return s;
    }

    public static void main(String[] args) {
        long x = hhmmss2ss("000001");
        System.out.println("x = " + x);

        x = hhmmss2ss("000101");
        System.out.println("x = " + x);

        x = hhmmss2ss("010001");
        System.out.println("x = " + x);

        x = hhmmss2ss("111111");
        System.out.println("x = " + x);
    }


    public String getTipo() {
        return  linea.substring(getTipoDesde(), getTipoHasta());
    }

    public String getLinea() {
        return linea;
    }

    /**
     * Compara fecha-hora, nroOrigen y tipo
     * devuelve 0 si soy iguales
     * negativo si este registro es menor que el recibodo como parámetro.
     * positivo si este registro es mayor que el recibodo como parámetro.
     */
    public int compareTo(Reg r2){
        int v = getFecha().compareTo(r2.getFecha());
        if (v != 0)
            return v;

        //misma fecha
        v = getNumOrigen().compareTo(r2.getNumOrigen());
        if (v != 0)
            return v;

        //misma fecha y nroOrigen
        return getTipo().compareTo(r2.getTipo());
    }
}
