package p.aplic.peliculas.imdb.loaders;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: JPB
 * Date: Apr 22, 2009
 * Time: 8:30:14 AM
 */
public class Util {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        adHoc();
        borrarIMDBDirecPelisRepetidos();
    }

    public static void borrar(String dir, String peli, String año, Connection conn) throws SQLException, ClassNotFoundException {
        long ini = System.currentTimeMillis();
        String sql = "DELETE FROM IMDB_DIRECTOR_PELICULA WHERE dir=? and peli=? and año=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, dir);
        ps.setString(2, peli);
        ps.setString(3, año);
        int i = ps.executeUpdate();

        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Demoró " + seg + " segundos, para i = " + i);
    }

    public static void borrarIMDBDirecPelisRepetidos() throws ClassNotFoundException, SQLException {
        Connection conn = Util.getConnection();
        int commitCada = 1000;
        conn.setAutoCommit(false);
        long ini = System.currentTimeMillis();
        int cantBorrados = -1;
        String sql = "SELECT count(*) as c, min(id) as minId, max(id), dir, peli, año " +
                "FROM IMDB_DIRECTOR_PELICULA " +
//                " group by dir, peli, año having c > 1 " ;
                " group by dir, peli, año having c > 1 LIMIT 10000" ;

        String sqlBorrar = "DELETE FROM IMDB_DIRECTOR_PELICULA WHERE ID = ?";
        PreparedStatement psB = conn.prepareStatement(sqlBorrar);

        PreparedStatement psS = conn.prepareStatement(sql);
        while(cantBorrados != 0){
            cantBorrados = 0;
            System.out.println("buscando...");            
            ResultSet rs = psS.executeQuery();
            List aBorrar = new ArrayList();
            while(rs.next() ){
                BigDecimal cant = rs.getBigDecimal(1);

                aBorrar.add(rs.getBigDecimal(2)) ;


                if (cant.intValue() > 2)
                    aBorrar.add(rs.getBigDecimal(3)) ;


                String dir = rs.getString(4);
                String pel = rs.getString(5);
                String año = rs.getString(6);
                borrar(dir, pel, año, conn);
                System.out.println("___________________________");
                System.out.println("cant = " + cant);
                System.out.println("dir = " + dir);
                System.out.println("pel = " + pel);
                System.out.println("año = " + año);
                System.out.println("___________________________");
//                cantBorrados++ ;//todo
            }
            System.out.println("borrando...");

//            for(Object o : aBorrar) {
//                psB.setBigDecimal(1, (BigDecimal)o);
//                psB.executeUpdate();
//                cantBorrados++;
//                if (cantBorrados % commitCada == 0)
//                    conn.commit();
//            }
            conn.commit();            
            System.out.println("Se (->) borraron " + cantBorrados + " registros.");
        }
        conn.commit();
        conn.close();
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Se borraron " + cantBorrados + " registros.");
        System.out.println("Demoró " + seg + " segundos");
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.gjt.mm.mysql.Driver");
        return DriverManager.getConnection (
                "jdbc:mysql://localhost/prueba","root", "juan");
    }

    public static String truncar(String x, int tamMax){
        if (x != null && x.length() > tamMax){
            String suf = "--TRUNCADO--";
            x = x.substring(0, tamMax-suf.length()) + suf;
        }
        return x;
    }
}
