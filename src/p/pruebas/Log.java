package p.pruebas;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Enumeration;

/**
 * User: JPB
 * Date: 5/11/11
 * Time: 8:50 AM
 */
public class Log {

    public static void main(String[] args) {
        new Log().probar();
    }

    private void probar(){
        Logger log = Logger.getLogger(Log.class);
        long cant = 2000;
        String aLoggear = "En el for_";
        Enumeration allAppenders = log.getAllAppenders();
        while(allAppenders.hasMoreElements()) {
            Object o = allAppenders.nextElement();
            System.out.println("appender = " + o);
        }

        for(int i = 0; i <cant; i++) {
            log.info(aLoggear + i + "_" + new Date());
        }
    }
}
