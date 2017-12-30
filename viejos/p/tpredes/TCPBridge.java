package p.tpredes;

import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPBridge extends Thread
    /*implements nt.util.event.StreamEventListener*/{

	private String _serverName;
	private int[] _serverPorts;
	private int _portToListen;

	private ServerSocket miself;
	private int bufferSize;
    private static final int defaultBufferSize = 4096;


public TCPBridge(String serverName, int[] serverPorts, int portToListen) {
	this(serverName, serverPorts, portToListen, defaultBufferSize);
}


public void run() {

	boolean forever = true;

	try {
		miself = new ServerSocket(_portToListen);

		while (forever) {
			try {
				//Se acepta el pedido
				Socket request = miself.accept();

				//Se crea un StreamConnector para que se encargue de la
				//conexion
                MultipleStream ms =
                    new MultipleStream(_serverName, _serverPorts, bufferSize);
				StreamConnector conexion =
                    new StreamConnector(ms, request.getInputStream(), bufferSize);
				conexion.setName("TCPConnector");
//                conexion.addStreamListener(this);
				conexion.start();

			}
			catch (java.io.IOException ioex) {
				System.out.println(
					"Ha ocurrido una excepcion al recibir el requerimiento: " + ioex.getMessage());
                ioex.printStackTrace();
			}
		}
	}
	catch (java.net.BindException be) {
        System.out.println("Problemas con el port " + _portToListen);
        be.getMessage();
    }
	catch (java.io.IOException ioe) {
		System.out.println(
			"Ha ocurrido una excepcion al iniciar las conexiones: " + ioe.getMessage());
        ioe.printStackTrace();
	}
}


public TCPBridge( String serverName, int[] serverPort, int portToListen,
					int bufferSize) {

	this._serverName = serverName;
	this._serverPorts = serverPort;
	this._portToListen = portToListen;
	this.bufferSize = bufferSize;
}

/**
 * Levanta un archivo de configuracion pasado como parametro, donde
 * lee los parametros de los TCPBridges que debe levantar.
 *
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
    //Tamaño de los buffers a usar
    int BUFFER_SIZE = 4096;


    //Caso en el que no se especifica el archivo de entrada
    if (args.length < 1) {
        System.out.println("Modo de uso: TCPBridge iniFilename");
        System.exit(0);
    }

    String filename = args[0];

    //Se abre el archivo de configuracion
    nt.util.TagFile tg = new nt.util.TagFile(filename);

    String baseServerName = "ServerName";
    String baseServerPort = "ServerPort";
    String baseListenPort = "ListenPort";
    String baseTamBuffer = "TamBuffer";

    int i = 0;
    String serverName;
    String serverPort;
    String listenPort;
    String tamBuffer;

    //Recorro el archivo de configuración
    while (true) {
        serverName = baseServerName + i;
        serverPort = baseServerPort + i;
        listenPort = baseListenPort + i;
        tamBuffer = baseTamBuffer + i;

        String serverNameValue = tg.readTag(serverName);

        if (serverNameValue != null) {
            int tamBufferValue = defaultBufferSize;
            String tbv = tg.readTag(tamBuffer);
            if (tbv != null && !tbv.trim().equals(""))
                tamBufferValue = Integer.parseInt(tbv);
            int listenPortValue = Integer.parseInt(tg.readTag(listenPort));
            String puertos = tg.readTag(serverPort);
            StringTokenizer st = new StringTokenizer(puertos, ",");
            int cant = st.countTokens();
            int[] ports = new int[cant];
            int j = 0;
            while (st.hasMoreTokens()) {
                String t = st.nextToken().trim();
                ports[j++] = Integer.parseInt(t);
            }

            //Una vez levantado los parámetros, creo un TCPBridge
            TCPBridge bridge = new TCPBridge(serverNameValue, ports,
                                             listenPortValue,
                                             tamBufferValue);
            bridge.start();
        } else
            break;
        i++;
    }

}

}