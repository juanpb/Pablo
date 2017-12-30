package p.pruebas;

import java.sql.*;

/**
 * User: JPB
 * Date: 07/11/16
 * Time: 16:34
 */
public class SQLServer {
    private static final String query = "select distinct bg_status from [certificacion_2016_db].[dbo].bug ";

    public static void main(String[] args)
    {
        //String name="cmscim";
        //String filename = "D:\\programs\\Tomcat 6.0\\webapps\\timescape\\canteen_scheduller\\CMS_CSV\\cms_cim\\"+ name+"-"+cal.get(Calendar.YEAR) +"-" +(cal.get(Calendar.MONTH)+1) + "-"+cal.get(Calendar.DATE)+".csv";
        Connection conn = null;
        String url = "jdbc:sqlserver://ashpqcrl01:1433;databasename=certificacion_2016_db;integratedSecurity=true";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String userName = "";
        String password = "";
        Statement stmt;
        try
        {

            Class.forName(driver);//.newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String x = rs.getString(1);
                System.out.println("x = " + x);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (conn!= null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}
