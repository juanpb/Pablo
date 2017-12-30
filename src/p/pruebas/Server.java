package p.pruebas;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * User: JP
 * Date: 30/07/2005
 * Time: 02:25:08
 */
public class Server {
    private static final Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        new Server();
    }

    public Server() throws IOException {
        ServerSocket s = new ServerSocket(1666);
        while(true){
            Socket so = s.accept();
            System.out.println("llego");
            hacer(so);
        }
    }

    private void hacer(final Socket so) {
        new Thread(new Runnable(){
            public void run() {
                StringBuffer sb = SerialNum.get();
                try {
                    byte[] z = sb.toString().getBytes();
                    System.out.println("Devolverá = " + new String(z));
                    so.getOutputStream().write(z);
                    so.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }).start();
    }
}
