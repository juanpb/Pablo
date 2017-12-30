package p.pruebas;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

/**
 * User: JP
 * Date: 18/07/2005
 * Time: 23:21:01
 */
public class MySQL {
    private static final Logger logger = Logger.getLogger(MySQL.class);
    private Connection _conn;
    public static void main(String[] args) {
        MySQL mySQL = new MySQL();
//        mySQL.insertFoto();
        mySQL.mostrarFoto();
        System.out.println("Listo");
    }

    private void mostrarFoto() {
        try {
            String sql = "SELECT * FROM users where id = ?";
            PreparedStatement ps = _conn.prepareStatement(sql);
            ps.setString(1, "id-");
            ResultSet rs = ps.executeQuery();

            String id = null;
            byte[] bytes = null;
            Blob blob = null;
            String pw = null;
            if (rs.next()) {
                id = rs.getString(1);
//                blob = rs.getBlob(2);
//                bytes = blob.getBytes(0, (int)blob.length());
                bytes = rs.getBytes(2);
                System.out.println("bytes.length = " + bytes.length);
                pw = rs.getString(3);
            }


            ImageIcon ii = new ImageIcon(bytes);
            mostar(ii, id, pw);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (_conn != null)
                try {
                    _conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
        }

    }

    private void mostar(ImageIcon ii, String id, String pw) {
        JDialog dlg = new JDialog();
        dlg.setTitle(id + " - " + pw);
        JButton jButton = new JButton(ii);
        dlg.getContentPane().add(jButton);
        dlg.setVisible(true);

    }

    private void insertFoto() {
        try {
            String sql = "INSERT INTO users VALUES(?, ?, ?)";
            PreparedStatement ps = _conn.prepareStatement(sql);
            ps.setString(1, "id-");
            File f = new File("F:\\tem\\Kraftwerk\\2005 - Minimum - Maximum\\kraftwerk_-_minimum_maximum_live_-_gsa_-_version_a.jpg");
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            byte[] foto = new byte[(int)f.length()];
            bis.read(foto);

            ps.setBytes(2, foto);
            ps.setString(3, "pw--");
            ps.execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (_conn != null)
                try {
                    _conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }

    /*
        SOBRE EL SERVER DE LA BD

NOTE: If you install MySQL in a folder other than
C:\MYSQL or you intend to start MySQL on NT/Win2000
as a service, you must create a file named C:\MY.CNF
or \Windows\my.ini or \winnt\my.ini with the following
information::

[mysqld]
basedir=E:/installation-path/
datadir=E:/data-path/

After your have installed MySQL, the installation
directory  will contain 4 files named 'my-small.cnf,
my-medium.cnf, my-large.cnf, my-huge.cnf'.
You can use this as a starting point for your own
C:\my.cnf file.
---------------



To start the server, enter this command:
C:\> F:\mysql\bin\mysqld --console
---
You can stop the MySQL server by executing this command:
C:\> F:\mysql\bin\mysqladmin -u root shutdown
---
Para ponerlo como servicio
mysqld --install mysql
---
Verificar:
dos: mysql
mysql> show databases;
para salir:
mysql> exit;
///////
Para usar desde java
Poner en el classpath: mysql-connector-java-3.0.15-ga-bin.jar
Una vez creado un esquema en la BD, dar permisos:
usando 'MySQL Control Center':
->'User Administrator' -> 'New User' -> y crear usuario: us , password: pp


Código:
DriverManager.getConnection(url);
con
url = <protocol>:<subprotocol>:<subname>
ej:  DriverManager.getConnection("jdbc:mysql://localhost/esqema?user=us&password=pp");
<subname> = //<host>[:<port>][/<databaseName>]

Connection Properties:
NAME			DESCRIPTION				DEFAULT
user			The username for the connection.	None
password		The password for the user.		None
autoReconnect		Set to true if the connection should
			automatically be reconnected.		false
maxReconnects		If autoReconnect=true, represents
			the total reconnect attempts.		3

initialTimeout		If autoReconnect=true, represents
			the time to wait (in seconds)
			between reconnect attempts.		2

maxRows			Limits the total number of rows to be
			returned by a query.			0 (maximum)
useUnicode		If true, the server will use
			Unicode when returning strings;
			otherwise, the server attempts to
			use the character set that is being
			used on the server.			true
characterEncoding	If useUnicode=true, specifies the
			encoding to be used.			None
relaxAutoCommit		If relaxAutoCommit=true, then the
			server allows transaction calls
			even if the server doesn't support
			transactions.				false
capitalizeTypeNames	If set to true, type names will be
			capitalized in DatabaseMetaData
			results.				false
profileSql		If set to true, queries and timings
			will be dumped to STDERR.		false
socketTimeout		If > 0 in milliseconds, the driver
			will drop the connection when the
			timeout expires and return the
			SQLState error of 08S01.		0
StrictFloatingPoint	If set to true, the driver will
			compensate for floating float
			rounding errors in the server.		false

    */


    public MySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String esqema = "cc";
            _conn = DriverManager.getConnection("jdbc:mysql://localhost/" +
                    esqema + "?user=us&password=pp");

//            String sql = "SELECT * FROM TABLA_UNO";
//            PreparedStatement ps = _conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                System.out.println("rs.getInt(1) = " + rs.getInt(1));
//                System.out.println("rs.getString(2) = " + rs.getString(2));
//                System.out.println("rs.getString(3) = " + rs.getString(3));
//                System.out.println("");
//            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
//        finally{
//            if (_conn != null)
//                try {
//                    _conn.close();
//                } catch (SQLException e) {
//                    logger.error(e.getMessage(), e);
//                }
//        }
    }
}
