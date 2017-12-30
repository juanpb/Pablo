package p.dm;

import org.apache.log4j.Logger;
import p.util.Fechas;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.sql.*;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: JPB
 * Date: Oct 2, 2008
 * Time: 2:44:03 PM
 */
public class Main2 {
    private static final Logger logger = Logger.getLogger(Main2.class);
    private static String entrada = "D:\\P\\_env\\DB\\partida\\a.txt35";

    private static String separador = ",";

    private static String count = "select count(*) from tabla_1";


    private static String insert = "INSERT INTO tabla_1 (CUIT, DENOMINACION, CUIT_CALLE, " +
            "CUIT_CALLE_NRO, CUIT_CALLE_PISO, CUIT_CALLE_DPTO, CUIT_LOCALIDAD, CUIT_CODIGO_POSTAL, " +
            "CUIT_PROVINCIA, CUIL, APELLIDO_NOMBRE, " +
            "CUIL_CALLE, CUIL_CALLE_NRO, CUIL_CALLE_PISO, CUIL_CALLE_DPTO, CUIL_LOCALIDAD, " +
            "CUIL_CODIGO_POSTAL, CUIL_PROVINCIA, CUIL_FECHA_NACIMIENTO, " +
            "CUIL_SEXO, PERIODO, CATEGORIA_REMUNERACION, REMUNERACION, nro_arch) VALUES(" +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?)";

    private static Connection con;
    private static PreparedStatement stmtIns;
    public static void main(String[] args) throws Exception {
        try{
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");// para Access
            Class.forName("org.gjt.mm.mysql.Driver");   //para mySQL
            con = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "a");
            //con = DriverManager.getConnection("jdbc:odbc:access", "", "");// para Access
//            con.prepareStatement("delete from tabla_1").executeUpdate();
//            con.commit();
            stmtIns = con.prepareStatement(insert);
            entrada = "D:\\P\\_env\\DB\\partida\\a.txt";
            long ini = System.currentTimeMillis();
            for(int i = 3; i < 36; i++) {       //hecho a.txt12
//            for(int i = 3; i < 4; i++) {       //hecho a.txt12
                String entrada2 = entrada + i;
                System.out.println("Archivo: " + entrada2);
                todo(new File(entrada2), i);
            }

            long fin = System.currentTimeMillis();
            String tiempo = Fechas.getTiempo(fin - ini);

            System.out.println("Demoró " + tiempo + " segundos");

        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally {
            if (con != null)
                con.close();
        }
    }

    private static boolean probarCount() throws SQLException {
        PreparedStatement st = con.prepareStatement(count);
        ResultSet rs = st.executeQuery();
        if (rs.next()){
            System.out.println("rs.getObject(1) = " + rs.getObject(1));
            return true;
        }
        return false;
    }

    public static void todo(File f, int nroArch) throws Exception {
        LineNumberReader lnr = null;
//        BufferedOutputStream bos = null;
        int cont = 0;
//        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        con.setAutoCommit(false);
        PreparedStatement st=null;
        try{
            lnr = new LineNumberReader(new FileReader(f));
//            bos = new BufferedOutputStream(new FileOutputStream(f.getAbsolutePath() + ".sal"));
            while(lnr.ready()){
                Registro reg = parsearLinea(lnr.readLine());
                if (reg != null){

//                    String s = reg.toString(Constantes.TAB_CHARACTER + "") + Constantes.NUEVA_LINEA;
//                    bos.write(s.getBytes());
//                    int tam = reg.getAtributos().size();
//                    Integer c = map.get(tam);
//                    if (c == null)
//                        c = 0;
//                    c++;
//                    map.put(tam, c);


                    List<Object> atrs = reg.getAtributos();
                    atrs.add(nroArch);
                    st = getStmt(atrs);
                    st.addBatch();
                    cont++;
                    if (cont % 5000 == 0){
                        System.out.println("Van " + cont);
                        st.executeBatch();
                        con.commit();
                    }
                }
            }
        }
        finally{
            if (st != null)
                st.executeBatch();
            con.commit();
            if (lnr != null)
                lnr.close();
//            if (bos != null)
//                bos.close();
        }
    }

    private static PreparedStatement getStmt(List<Object> atrs){
        int i = 0;
        try {
            for(Object o : atrs) {
                stmtIns.setObject(++i, o);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        return stmtIns;
    }

    private static Registro parsearLinea(String s){
        if (s == null || s.trim().equals(""))
            return null;
        String copia = s;
        Registro reg = new Registro();
        //son 23 campos separados por coma. Los primeros 21 están entrecomillados.
        try {
            for(int i = 0; i < 21; i++) {
                int d = s.indexOf("\"")+1;
                int h = s.indexOf("\",", d);
                String atr = s.substring(d, h).trim();
                reg.add(atr);
                s = s.substring(h+2);
            }
        } catch (Exception e) {
            System.out.println("copia = " + copia);
            logger.error(e.getMessage(), e);
        }

        //busco los 2 que faltan
        int h = s.indexOf(separador);
        String o = s.substring(0, h);
        reg.add(o);
        o = s.substring(h+1);
        reg.add(o);

        if (o.contains(",")){
            System.out.println("Problemas con " + copia);
            System.out.println("o = " + o);
            System.out.println("___");
            int cont=0;
            for(Object o1 : reg.getAtributos()) {
                System.out.println(cont + " = " + o1);
                cont++;
            }
        }


        return reg;
//        StringTokenizer st = new StringTokenizer(s, separador);
//        return token2Registro(st);
    }

    private static Registro token2Registro(StringTokenizer st){
        Registro reg = new Registro();

        while(st.hasMoreTokens()) {
            String s = st.nextToken();

            //saco comillas
            if (s.startsWith("\""))
                s = s.substring(1);
            if (s.endsWith("\"") )
                s = s.substring(0, s.length()-1);

            //trim
            s = s.trim();

            reg.add(s);
        }

        return reg;
    }

}

