package p.aplic.peliculas.imdb.loaders;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import p.aplic.peliculas.imdb.ParserDirectors;
import p.aplic.peliculas.imdb.Peli;
import p.aplic.peliculas.imdb.Persona;
import p.util.Constantes;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: JPB
 * Date: Apr 22, 2009
 * Time: 8:31:58 AM
 */
public class Directors {
    private static final Logger logger = Logger.getLogger(Directors.class);

    public static void main(String[] args) throws ClassNotFoundException, SQLException, JDOMException, IOException {
        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        subirMoviesDirectors();
    }

//    public static void subirLocalDirectors() throws ClassNotFoundException, SQLException, JDOMException, IOException {
//        Connection conn = Util.getConnection();
//        String sql = "INSERT INTO DIRECTOR (NOMBRE) VALUES (?)";
//        PreparedStatement ps = conn.prepareStatement(sql);
//        Set<Persona> ms = ParserDirectors.getLocalList();
//        int cont = 0;
//        for(Persona m : ms) {
//            ps.setString(1, m.getNombre());
//            ps.execute();
//            cont++;
//        }
//        System.out.println("Se cargaron " + cont + " directores.");
//    }

//    public static void subirLocalMoviesDirectors() throws ClassNotFoundException, SQLException, JDOMException, IOException {
//        Connection conn = Util.getConnection();
//
//        String sql = "INSERT INTO DIRECTOR_PELICULA (DIR_ID, PELI_ID) VALUES (?,?)";
//        PreparedStatement ps = conn.prepareStatement(sql);
//        Set<Persona> ms = ParserDirectors.getLocalList();
//        int cont = 0;
//        long ini = System.currentTimeMillis();
//        System.out.println("Subiendo...");
//        for(Persona m : ms) {
//            ps.setInt(1, Model.getDirectorId(m.getNombre()));
//            for(Peli p : m.getPelis()) {
//                int movieId = Model.getMovieId(p.getNombre(), p.getAño());
//                ps.setInt(2, movieId);
//                try {
//                    ps.execute();
//                } catch (SQLException e) {
//                    System.out.println("Problemas con dir_id,dir_nom, mov_id, mov_no: "+
//                      Model.getDirectorId(m.getNombre()) + ", " + m.getNombre() + ", " +
//                      movieId + ", " + p.getNombre() );
//                    throw e;
//                }
//                cont++;
//            }
//
//        }
//
//        long fin = System.currentTimeMillis();
//        long seg = (fin - ini)/1000;
//        System.out.println("Se cargaron " + cont + " pel_directores en " + seg + " seg.");
//    }


    @SuppressWarnings({"UnusedAssignment"})
    public static void subirMoviesDirectors() throws ClassNotFoundException, SQLException {
        String filePath = System.getProperty("directorsListPath");
        int cont = 0;
        long ini = System.currentTimeMillis();
        System.out.println("Subiendo en IMDB_DIRECTOR_PELICULA...");
        Connection conn = Util.getConnection();
        LineNumberReader lnr = null;
        try{
            lnr = new LineNumberReader(new FileReader(filePath));
            boolean seguir = true;

            //ignoro las primeras líneas
            while(seguir){
                String linea = lnr.readLine();
                if (linea != null && linea.equals(ParserDirectors.IGNORAR_HASTA))
                    seguir = false;
                linea = null;
            }
            seguir = true;
            Persona d=null;
            while(seguir){
                String linea = lnr.readLine();
                if (linea.equals(ParserDirectors.IGNORAR_DESDE))
                    seguir = false;
                else{
                    while(linea.startsWith(Constantes.TAB_CHARACTER+"")){
                        Peli m = ParserDirectors.getMovie(linea);
                        if (m != null){
                            d.addPeli(m);
                        }
                        linea = lnr.readLine();
                    }
                    if (d != null){
                        if (cont > 160000)    //todo sacar
                            insertarIMDBDir(d, conn);
                        cont++;
                        d = null;
                        if (cont % 1000 == 0)
                            System.out.println("cont = " + cont);
                    }
                    String n = ParserDirectors.getNombre(linea);
                    if (n != null){
                        d = new Persona();
                        d.setNombre(n);
                        //en la misma línea hay nombre de pel
                        linea = linea.substring(n.length()).trim();
                        Peli m = ParserDirectors.getMovie(linea);
                        if (m != null)
                            d.addPeli(m);
                    }
                    linea = null;
                }
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally{
            if (conn != null)
                conn.close();

            if (lnr != null)
                try {
                    lnr.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Se cargaron " + cont + " IMDB_DIRECTOR_PELICULA en " + seg + " seg.");
    }

    private static void insertarIMDBDir(Persona d, Connection conn)
            throws ClassNotFoundException, SQLException {


        String sql = "INSERT INTO IMDB_DIRECTOR_PELICULA (DIR, PELI, AÑO) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(Peli p : d.getPelis()) {
            ps.setString(1, d.getNombre());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getAño());
            try {
                ps.execute();
            } catch (SQLException e) {
                System.out.println("Problemas con sql: "+ sql + ", ?1= " + d.getNombre() +
                        ", ?2=" + p.getNombre() + ", ?3=" + p.getAño());
                logger.error(e.getMessage(), e);
                System.out.println("_______________________________________________________");
            }
        }
    }
}

