package p.aplic.peliculas.util;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * User: JPB
 * Date: Mar 21, 2007
 * Time: 7:00:56 PM
 */
public class Log {
    private static final Logger logger = Logger.getLogger(Log.class);

    public static void error(Exception e){
        JOptionPane.showMessageDialog(null, e.getMessage());
        logger.error(e.getMessage(), e);
    }

    public static void mensaje(String s) {
        JOptionPane.showMessageDialog(null, s);
    }
}
