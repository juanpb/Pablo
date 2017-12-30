package p.aplic.peliculas.imdb;

import p.aplic.peliculas.imdb.loaders.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: JPB
 * Date: Apr 22, 2009
 * Time: 9:06:06 AM
 */
public class Model {
    private static final String sqlSelectDirId = "SELECT ID FROM DIRECTOR WHERE NOMBRE = ?" ;
    private static final String sqlSelectMovieID = "SELECT ID FROM PELICULA WHERE NOMBRE = ? and año = ?" ;
    private static final String sqlSelectActorId = "SELECT ID FROM ACTOR WHERE NOMBRE = ? " ;


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        prueba();
    }

    private static void prueba() throws SQLException, ClassNotFoundException {
        Connection conn = Util.getConnection();
        String sql = "SELECT count(*) as c FROM IMDB_DIRECTOR_PELICULA" ;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next() )
            System.out.println("rs.getBigDecimal(1) = " + rs.getBigDecimal(1));
        else
            System.out.println("no next");
    }

    public static int getDirectorId(String nombre, Connection conn) throws ClassNotFoundException, SQLException {
        PreparedStatement psSelectDirId = conn.prepareStatement(sqlSelectDirId);
        psSelectDirId.setString(1, nombre);
        ResultSet rs = psSelectDirId.executeQuery();
        int res = 0;
        if (rs.next())
            res = rs.getInt(1);
        if (rs.next())
            System.out.println("Inconsistencia. Hay más de un director: '" + nombre +"'");
        rs.close();
        psSelectDirId.close();
        return res;
    }

    public static int getMovieId(String nombre, String año, Connection conn)
            throws ClassNotFoundException, SQLException {
        PreparedStatement ps = conn.prepareStatement(sqlSelectMovieID);
        ps.setString(1, nombre);
        ps.setString(2, año);
        ResultSet rs = ps.executeQuery();
        int res = 0;
        if (rs.next())
            res = rs.getInt(1);
        if (rs.next())
            System.out.println("Advertencia. Hay más de una peli: '" + nombre +"'");
        rs.close();
        ps.close();
        return res;
    }

    public static int getActorId(String nombre, Connection conn)
            throws ClassNotFoundException, SQLException {
        PreparedStatement ps = conn.prepareStatement(sqlSelectActorId);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        int res =0;
        if (rs.next())
            res = rs.getInt(1);
        if (rs.next())
            System.out.println("Advertencia. Hay más de un actor con nombre: '" + nombre +"'");
        rs.close();
        ps.close();

        return res;
    }
}
