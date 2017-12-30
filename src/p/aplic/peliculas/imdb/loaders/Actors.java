package p.aplic.peliculas.imdb.loaders;

import org.apache.log4j.Logger;
import p.aplic.peliculas.imdb.*;
import p.util.Constantes;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * User: JPB
 * Date: Apr 22, 2009
 * Time: 8:31:58 AM
 */
public class Actors {
    private static final Logger logger = Logger.getLogger(Actors.class);

    private static int contAct = 0;
    private static int contActPel = 0;
    private static int contActPelIMDB = 0;
    private static String linea;
    private static PreparedStatement psInsertActPeli;
    private static PreparedStatement psInsertIMDBActPeli;
    private static PreparedStatement psInsertActor;
    private static String sqlInsertActPeli = "INSERT INTO ACTOR_PELICULA (ACTOR_ID, PELI_ID, PAPEL, PUESTO) VALUES (?,?,?,?)";
    private static String sqlInsertIMDBActPeli = "INSERT INTO IMDB_ACTOR_PELICULA " +
                "(actor_nombre, peli_nombre, peli_año, papel, puesto) VALUES (?,?,?,?,?)";
    private static String sqlInsertActor = "INSERT INTO ACTOR (nombre) VALUES (?)";



    public static void main(String[] args) throws Exception {
        if (args.length != 1){
            String s = "Se debe pasar como parámetro el archivo de config.";
            JOptionPane.showMessageDialog(null, s);
            return ;
        }

        String configPath = args[0];
        p.util.Util.loadProperties(configPath);
        borrarTodo();
        try {
            subirActores();
        } catch (Exception e) {
            System.out.println("Última línea leída: '" + linea + "'");
            throw e;
        }
    }


    //borra toDo ACTOR
    public static void borrarActor(Connection conn) throws SQLException {
        String sql = "DELETE FROM ACTOR";
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = ps.executeUpdate();
        System.out.println(sql + " = " + i);
        ps.close();
    }
    //borra toDo ACTOR_PELICULA
    public static void borrarActorPel(Connection conn) throws SQLException{
        String sql = "DELETE FROM ACTOR_PELICULA";
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = ps.executeUpdate();
        System.out.println(sql + " = " + i);
        ps.close();
    }

    //borra toDo IMDB_ACTOR_PELICULA
    public static void borrarIMDBActPel(Connection conn) throws SQLException{
        String sql = "DELETE FROM IMDB_ACTOR_PELICULA";
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = ps.executeUpdate();
        System.out.println(sql + " = " + i);
        ps.close();
    }

    //borra toDo ACTOR, ACTOR_PELICULA y en IMDB_ACTOR_PELICULA
    public static void borrarTodo() throws ClassNotFoundException, SQLException {
        Connection conn = Util.getConnection();
        borrarActorPel(conn);
        borrarActor(conn);
        borrarIMDBActPel(conn);
        conn.close();
    }

    //sube en ACTOR, ACTOR_PELICULA y en IMDB_ACTOR_PELICULA
    public static void subirActores() throws ClassNotFoundException, SQLException, IOException {
        Connection conn = Util.getConnection();

        psInsertActPeli = conn.prepareStatement(sqlInsertActPeli);
        psInsertIMDBActPeli = conn.prepareStatement(sqlInsertIMDBActPeli);
        psInsertActor = conn.prepareStatement(sqlInsertActor);

        String filePath = System.getProperty("actorsListPath");
        long ini = System.currentTimeMillis();
        System.out.println("Subiendo en ACTOR, ACTOR_PELICULA y en IMDB_ACTOR_PELICULA...");

        conn.setAutoCommit(false);
        LineNumberReader lnr = null;
        try{
            lnr = new LineNumberReader(new FileReader(filePath));
            boolean seguir = true;

            //ignoro las primeras líneas
            while(seguir){
                linea = lnr.readLine();
                if (linea != null && linea.equals(ParserActor.IGNORAR_HASTA))
                    seguir = false;
                linea = null;
            }
            seguir = true;

            Actor d=null;
            while(seguir){
                linea = lnr.readLine();
                if (linea != null){
                    if (linea.equals(ParserActor.IGNORAR_DESDE)|| !lnr.ready())
                        seguir = false;
                    else{
                        while(linea!=null && linea.startsWith(Constantes.TAB_CHARACTER+"")){
                            Actuacion m = ParserActor.getActuacion(linea);
                            if (m != null)
                                d.addPeli(m);
                            linea = lnr.readLine();
                        }
                        if (d != null){
                            subir(d, conn);
                            d.setPelis(null);
                            d = null;
                        }
                        String n = ParserActor.getNombre(linea);
                        if (n != null){
                            d = new Actor();
                            d.setNombre(n);
                            //en la misma línea hay nombre de pel
                            String linea2 = linea.substring(n.length()).trim();
                            Actuacion m = ParserActor.getActuacion(linea2);
                            if (m != null)
                                d.addPeli(m);
                        }
                    }
                }
                else if (!lnr.ready())
                    seguir = false;
                linea = null;
            }
        }finally{
            if (conn != null){
                conn.commit();
//                conn.close();
            }

            if (lnr != null)
                try {
                    lnr.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
        long fin = System.currentTimeMillis();
        long seg = (fin - ini)/1000;
        System.out.println("Se cargaron " + contActPelIMDB + " en IMDB_ACTOR_PELICULA, " +
                " y " + contAct + " en ACTOR, " +  
                contActPel + " en ACTOR_PELICULA en " + seg + " seg.");
    }

    //sube en ACTOR, ACTOR_PELICULA y en IMDB_ACTOR_PELICULA
    private static void subir(Actor ac, Connection conn) throws ClassNotFoundException, SQLException {
        List<Actuacion> actuaciones = ac.getPelis();
        if (actuaciones == null)
            return ;
        for(Actuacion a : actuaciones) {
            int id = Model.getMovieId(a.getPeli().getNombre(), a.getPeli().getAño(), conn);
            if (id > 0){
                a.getPeli().setId(id);
                insertarLocalActor(ac.getNombre(), a, conn);
            }
            insertarIMDBActuacion(ac.getNombre(), a, conn);
        }
    }
    //Inserta en ACTOR_PELICULA y en ACTOR (si no está)
    private static void insertarLocalActor(String actor, Actuacion a, Connection conn) //aca
            throws ClassNotFoundException, SQLException {
        //ACTOR
        int actorId = insertarActorSiNoExiste(actor, conn);

         //ACTOR_PELICULA

        psInsertActPeli.setInt(1, actorId);
        psInsertActPeli.setInt(2, a.getPeli().getIdAsInt());
        String papel = a.getPapel();
        if (papel == null)
            papel = "---SIN_PAPEL---";
        psInsertActPeli.setString(3, papel);
        psInsertActPeli.setInt(4, a.getPuesto());
        try {
            psInsertActPeli.execute();
        } catch (SQLException e) {
            System.out.println("Problemas con sql: "+ sqlInsertActPeli + ", ?1= " + actorId + ", ?2=" +
                    a.getPeli().getIdAsInt() + ", ?3=" + a.getPapel()+ ", ?4=" + a.getPuesto());
            System.out.println("Linea: '" + linea + "'");
            logger.error(e.getMessage(), e);
        }
        contActPel++;
        if (contActPel % 100 == 0)
            System.out.println("insertados en ACTOR_PEL: " + contActPel);
    }

    private static int insertarActor(String actor, Connection conn) throws ClassNotFoundException, SQLException {
        psInsertActor.setString(1, actor);
        try {
            psInsertActor.execute();
        } catch (SQLException e) {
            System.out.println("Problemas con sqlInsertActor: "+ sqlInsertActor + ", ?1= " + actor);
            System.out.println("Linea: '" + linea + "'");
            logger.error(e.getMessage(), e);
        }
        contAct++;
        if (contAct % 100 == 0)
            System.out.println("insertados en ACTOR: " + contAct);
        return Model.getActorId(actor, conn);
    }

    private static int insertarActorSiNoExiste(String actor, Connection conn)
            throws ClassNotFoundException, SQLException {
        int id = Model.getActorId(actor, conn);
        if (id < 1)
            return insertarActor(actor, conn);
        return id;
    }

    private static void insertarIMDBActuacion(String actor, Actuacion a, Connection conn)
            throws ClassNotFoundException, SQLException {

        Peli peli = a.getPeli();
        psInsertIMDBActPeli.setString(1, actor);
        psInsertIMDBActPeli.setString(2, peli.getNombre());
        psInsertIMDBActPeli.setString(3, peli.getAño());
        psInsertIMDBActPeli.setString(4, a.getPapel());
        psInsertIMDBActPeli.setInt(5, a.getPuesto());
        try {
            psInsertIMDBActPeli.execute();
        } catch (SQLException e) {
            System.out.println("Problemas con sqlInsertIMDBActPeli: "+ sqlInsertIMDBActPeli + ", ?1= " + actor +
                    ", ?2=" + peli.getNombre() + ", ?3=" + peli.getAño() + ", ?4=" + a.getPuesto());
            System.out.println("Linea: '" + linea + "'");
            logger.error(e.getMessage(), e);
        }
        contActPelIMDB++;
        if (contActPelIMDB % 500 == 0){
            conn.commit();
            System.gc();
        }
        if (contActPelIMDB % 1000 == 0)
            System.out.println("insertados en IMDB_ACTOR_PELICULA: " + contActPelIMDB);
    }
}