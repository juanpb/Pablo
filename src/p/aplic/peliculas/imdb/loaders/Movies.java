package p.aplic.peliculas.imdb.loaders;

import p.aplic.peliculas.imdb.ParserMovies;
import p.aplic.peliculas.imdb.Peli;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * User: JPB
 * Date: Apr 21, 2009
 * Time: 3:09:35 PM
 */
public class Movies {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        subirLocalMovies();
    }

    public static void subirLocalMovies() throws ClassNotFoundException, SQLException {
        Connection conn = Util.getConnection();
        String sql = "INSERT INTO PELICULA (ID, NOMBRE, AÑO) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        Set<Peli> ms = ParserMovies.getLocalMovies();
        for(Peli m : ms) {
            ps.setInt(1, m.getIdAsInt());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getAño());
            ps.execute();
        }
    }
    
}
