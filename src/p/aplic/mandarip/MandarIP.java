package p.aplic.mandarip;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * User: Administrador
 * Date: 09/06/2006
 * Time: 00:22:30
 */
public class MandarIP {

    /**
     * Tiempo en minutos entre cada revisión
     */
    private long actualizarCada = 15 * 60 * 1000;
    private String ultimaIP = null;

    public static void main(String[] args) throws UnknownHostException {
        new MandarIP().getIP();
    }

    private String getIP() throws UnknownHostException {
        InetAddress ia = InetAddress.getLocalHost();
        return ia.getHostAddress();
    }

//    private String getIP2(){
//        String ip = null;
//        try {
//            InetAddress ia = InetAddress.getLocalHost();
//                System.out.println("ia.getAddress() = " + ia.getAddress());
//                System.out.println("ia.getCanonicalHostName() = " + ia.getCanonicalHostName());
//                System.out.println("ia.getHostAddress() = " + ia.getHostAddress());
//                System.out.println("ia.getHostName() = " + ia.getHostName());
//                System.out.println("---");
//
//
//             InetAddress[] allByName = InetAddress.getAllByName(null);
//             for (int i = 0; i < allByName.length; i++) {
//                ia = allByName[i];
//                System.out.println("ia.getAddress() = " + ia.getAddress());
//                System.out.println("ia.getCanonicalHostName() = " + ia.getCanonicalHostName());
//                System.out.println("ia.getHostAddress() = " + ia.getHostAddress());
//                System.out.println("ia.getHostName() = " + ia.getHostName());
//                System.out.println("---");
//            }
//        } catch (UnknownHostException e) {
//            log(e);
//        }
//        return ip;
//    }
//
//    private void log(Exception e) {
//        logger.error(e.getMessage(), e);
//    }
}
