package p.util;

public class Hora {

    public long todo = 0;
    public long ms = 0;
    public int seg = 0;
    public int min = 0;
    public int hora = 0;

    public static final long MS_EN_SEG = 1000;
    public static final long MS_EN_MIN = 60 * MS_EN_SEG;
    public static final long MS_EN_HORA = 60 * MS_EN_MIN;

    public Hora(){
        this(System.currentTimeMillis());
    }

    public Hora(long ms) {
        todo = ms;
        convertir(ms);
    }

    public Hora(int seg, long ms){
        this(seg * 1000 + ms);
    }

    public Hora(int min, int seg, long ms){
        this(min * 60 + seg, ms);
    }

    public Hora(int hora, int min, int seg, long ms){
        this(hora * 60 + min, seg, ms);
    }

    /**
     * Se asume: los dos primeros caracteres son la hora, luego un separador,
     * dos car. para los min, un separador, dos caracteres para los segundos y
     * opcionalmente, un separador más tres caracteres para los ms.
     */
    public Hora(String hhMmSsMs){
        this(0);
        if (hhMmSsMs == null || hhMmSsMs.length() < 8) return;

        hhMmSsMs = hhMmSsMs.trim();
        String h = hhMmSsMs.substring(0, 2);
        String m = hhMmSsMs.substring(3, 5);
        String s = hhMmSsMs.substring(6, 8);
        String mili = "0";
        if (h.equals("00"))
            h = "0";
        if (m.equals("00"))
            m = "0";
        if (s.equals("00"))
            s = "0";
        if (mili.equals("00"))
            mili = "0";
        if (hhMmSsMs.length() > 9){
            mili = hhMmSsMs.substring(9);
        }
       this.add(Integer.parseInt(h), Integer.parseInt(m), Integer.parseInt(s),
             Long.parseLong(mili));
    }

    /**
     * Diferencia en milisegundos entre la actual y otro =  actual - otro
     * @param otro
     * @return
     */
    public long diferencia(Hora otro){
        return diferenciaEnMiliseg(otro);
    }
    
    /**
     * Diferencia entre la actual y otro =  actual - otro
     * @param otro
     * @return
     */
    public long diferenciaEnMiliseg(Hora otro){
        return todo - otro.todo;
    }
    /**
     *
     * @return true si this es mayor que nh
     */
    public boolean esMayor(Hora nh){
        return hora > nh.hora
                | (hora == nh.hora && min > nh.min)
                | (hora == nh.hora && min == nh.min && seg > nh.seg)
                | (hora == nh.hora && min == nh.min && seg == nh.seg && ms > nh.ms);
    }

    public void restar(long ms){
        todo -= ms;
        convertir(todo);
    }
    public void add(long ms){
        todo += ms;
        convertir(todo);
    }

    public void set(long ms){
        todo = ms;
        convertir(ms);
    }

    public void add (int seg, long ms){
        add(seg * 1000 + ms);
    }

    public void add (int min, int seg, long ms){
        add(min * 60 + seg, ms);
    }

    public void add (Hora hora){
        add(hora.hora, hora.min, hora.seg, hora.ms);
    }
    public void add (int horas, int min, int seg, long ms){
        add(horas * 60 + min, seg, ms);
    }

    public void por(float x){
        float t = (float) this.todo;
        this.todo = (long)(t * x);
        convertir(this.todo);
    }

    public String toString(){
        return hora + ":" + min + ":" + seg;
    }

    public static Hora parse(long ms){
        int hora2 = (int) (ms / MS_EN_HORA);
        hora2 = hora2 % 24;
        ms = ms - hora2 * MS_EN_HORA;

        int min2 = (int)(ms / MS_EN_MIN);
        ms = ms - min2 * MS_EN_MIN;

        int seg2 = (int)(ms / MS_EN_SEG);
        ms = ms - seg2 * MS_EN_SEG;

        return new Hora(hora2, min2, seg2,  ms);
    }


    public Object clone(){
        return new Hora(hora, min, seg, ms);
    }
/*****************************************************************************/
/*  Métodos privados                                                         */
/*****************************************************************************/
    private void convertir(long ms){
        hora =  (int)(ms / MS_EN_HORA);
        hora = hora % 24;
        ms = ms - hora * MS_EN_HORA;

        min = (int)(ms / MS_EN_MIN);
        ms = ms - min * MS_EN_MIN;

        seg = (int)(ms / MS_EN_SEG);
        ms = ms - seg * MS_EN_SEG;

        this.ms = ms;
    }



    public static void main(String[] args) {
        Hora h = new Hora(15, 59, 30, 0);
        h.add(0, 61, 1, 0);
        System.out.println("hora : " + h.hora);
        System.out.println("min : " + h.min);
        System.out.println("seg : " + h.seg);
        System.out.println("ms : " + h.ms);
    }
}
