package p.pruebas.cmd;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserBloqueado {
    private static final Logger logger = Logger.getLogger(UserBloqueado.class);
    private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static String cmd = "net user USER /domain";

    public UserBloqueado(String usuario) {
        cmd = cmd.replace("USER", usuario.trim());
        logger.info("Se correrá el comando: " + cmd);
    }


    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1){
            System.out.println("Pasar como parámetro el nombre del usuario");
            System.exit(-1);
        }else{
            String usuario = args[0];
            UserBloqueado te = new UserBloqueado(usuario);
            te.dale();
        }
    }

    public void dale() throws InterruptedException {
        boolean estadoAnterior = bloqueado();
        loguear(estadoAnterior);

        while (true) {
            Thread.sleep(1000 * 60 * 1);
            boolean estadoActual = bloqueado();
            if (estadoActual != estadoAnterior){
                estadoAnterior = estadoActual;
                loguear(estadoAnterior);
            }
        }
    }

    private static void loguear(boolean estado){
        String msg;
        if (estado)
            msg = "Bloqueado";
        else
            msg = "No Bloqueado";
        logger.info(df.format(Calendar.getInstance().getTime()) + ": " + msg);
    }

    private static boolean bloqueado(){
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("Cuenta activa"))  {
                    logger.debug(line);
                    System.out.println(line);
                    if (line.startsWith("Cuenta activa                              S"))
                        return false;
                    else if (line.startsWith("Cuenta activa                              B"))
                        return true;
                    else {
                        logger.debug("linea no reconocida: '" + line + "'");
                        return true;
                    }
                }
                else{
                    if (line.startsWith("Account active"))  {
                        logger.debug(line);
                        if (line.startsWith("Account active               Y"))
                            return false;
                        else if (line.startsWith("Account active               Locked"))
                            return true;
                        else {
                            logger.debug("linea no reconocida: '" + line + "'");
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return true;
    }
}
