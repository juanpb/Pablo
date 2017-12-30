package p.proc;

import p.util.Constantes;
import p.util.UtilString;

/**
 * User: JPB
 * Date: 22/01/13
 * Time: 16:42
 */
public class GenerarParticiones {
    private static String comodin = "-comodin-";
    
    private static String MES = "02";

    private static String sql_MOVIMIENTOS = "ALTER TABLE ADC_MOVIMIENTOS\n" +
            " ADD \n" +
            "  PARTITION PRT_2013" + MES + comodin + " VALUES LESS THAN (TO_DATE(' 2013-" + MES + "-" + comodin +
            "  00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))\n" +
            "    LOGGING\n" +
            "    NOCOMPRESS\n" +
            "    TABLESPACE TA_ADC_DATA\n" +
            "    PCTFREE    10\n" +
            "    INITRANS   20\n" +
            "    MAXTRANS   250\n" +
            "    STORAGE    (\n" +
            "                INITIAL          1024M\n" +
            "                NEXT             16M\n" +
            "                MINEXTENTS       1\n" +
            "                MAXEXTENTS  UNLIMITED\n" +
            "                BUFFER_POOL      DEFAULT\n" +
            "               );\n\n";

    private static String sql_EXISTE_CDR = "ALTER TABLE ADC_EXISTE_CDR\n" +
            " ADD \n" +
            "  PARTITION PRT_2013" + MES + comodin + "00 VALUES LESS THAN (TO_DATE(' 2013-" + MES + "-" + comodin + " 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))\n" +
            "    LOGGING\n" +
            "    NOCOMPRESS\n" +
            "    TABLESPACE TA_ADC_DATA\n" +
            "    PCTFREE    10\n" +
            "    INITRANS   20\n" +
            "    MAXTRANS   255\n" +
            "    STORAGE    (\n" +
            "                INITIAL          16M\n" +
            "                NEXT             8M\n" +
            "               );\n\n" +
            "ALTER TABLE ADC_EXISTE_CDR\n" +
            " ADD \n" +
            "  PARTITION PRT_2013" + MES + comodin + "04 VALUES LESS THAN (TO_DATE(' 2013-" + MES + "-" + comodin + " 04:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))\n" +
            "    LOGGING\n" +
            "    NOCOMPRESS\n" +
            "    TABLESPACE TA_ADC_DATA\n" +
            "    PCTFREE    10\n" +
            "    INITRANS   20\n" +
            "    MAXTRANS   255\n" +
            "    STORAGE    (\n" +
            "                INITIAL          32M\n" +
            "                NEXT             8M\n" +
            "               );\n\n" +
            "ALTER TABLE ADC_EXISTE_CDR\n" +
            " ADD \n" +
            "  PARTITION PRT_2013" + MES + comodin + "08 VALUES LESS THAN (TO_DATE(' 2013-" + MES + "-" + comodin + " 08:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))\n" +
            "    LOGGING\n" +
            "    NOCOMPRESS\n" +
            "    TABLESPACE TA_ADC_DATA\n" +
            "    PCTFREE    10\n" +
            "    INITRANS   20\n" +
            "    MAXTRANS   255\n" +
            "    STORAGE    (\n" +
            "                INITIAL          32M\n" +
            "                NEXT             8M\n" +
            "               );\n\n" +
            "ALTER TABLE ADC_EXISTE_CDR\n" +
            " ADD \n" +
            "  PARTITION PRT_2013" + MES + comodin + "12 VALUES LESS THAN (TO_DATE(' 2013-" + MES + "-" + comodin + " 12:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))\n" +
            "    LOGGING\n" +
            "    NOCOMPRESS\n" +
            "    TABLESPACE TA_ADC_DATA\n" +
            "    PCTFREE    10\n" +
            "    INITRANS   20\n" +
            "    MAXTRANS   255\n" +
            "    STORAGE    (\n" +
            "                INITIAL          32M\n" +
            "                NEXT             8M\n" +
            "               );\n\n" +
            "ALTER TABLE ADC_EXISTE_CDR\n" +
            " ADD \n" +
            "  PARTITION PRT_2013" + MES + comodin + "16 VALUES LESS THAN (TO_DATE(' 2013-" + MES + "-" + comodin + " 16:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))\n" +
            "    LOGGING\n" +
            "    NOCOMPRESS\n" +
            "    TABLESPACE TA_ADC_DATA\n" +
            "    PCTFREE    10\n" +
            "    INITRANS   20\n" +
            "    MAXTRANS   255\n" +
            "    STORAGE    (\n" +
            "                INITIAL          32M\n" +
            "                NEXT             8M\n" +
            "               );\n\n" +
            "ALTER TABLE ADC_EXISTE_CDR\n" +
            " ADD \n" +
            "  PARTITION PRT_2013" + MES + comodin + "20 VALUES LESS THAN (TO_DATE(' 2013-" + MES + "-" + comodin + " 20:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))\n" +
            "    LOGGING\n" +
            "    NOCOMPRESS\n" +
            "    TABLESPACE TA_ADC_DATA\n" +
            "    PCTFREE    10\n" +
            "    INITRANS   20\n" +
            "    MAXTRANS   255\n" +
            "    STORAGE    (\n" +
            "                INITIAL          32M\n" +
            "                NEXT             8M\n" +
            "               );\t";


    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String nuevo;
        for (int i = 1; i <= 28; i++) {
            nuevo = get2Digitos(i);
            String x = UtilString.reemplazarTodo(sql_MOVIMIENTOS, comodin, nuevo);
            sb.append(x);
            sb.append(Constantes.NUEVA_LINEA);
        }
        System.out.println("__________________________________________________");

        System.out.println(sb.toString());

        System.out.println("__________________________________________________");
    }

    private static String get2Digitos(int i){
        if (i < 10)
            return "0" + i;
        else
            return "" + i;
    }
}
