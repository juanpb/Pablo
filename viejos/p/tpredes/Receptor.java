package p.tpredes;

import p.tpredes.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class Receptor extends Thread{

    private ServerSocket _serverSocket;
    private List _mensajes = new Vector();
    private Servidor _servidor = null;

    public Receptor(ServerSocket s){
        this(s, null);
    }
    public Receptor(ServerSocket s, Servidor ser){
        _serverSocket = s;
        _servidor = ser;
    }

    public void run(){
        ObjectInputStream in = null;
        int port = _serverSocket.getLocalPort();
        log("Esperando conexión en puerto: " + port);
        Socket socket = null;
        try{
            socket = _serverSocket.accept();
            log("Entró conexión en port = " + port);
            in = new ObjectInputStream(socket.getInputStream());
            int bytesReaded;
            while(true){
              Mensaje c = (Mensaje)in.readObject();
              System.out.println("Llegó el msg = " + c.getOffeset() +
                                 " en el port = " + port);
              _mensajes.add(c);

              //Se permite a otros threads ejecutar
              this.yield();
            }
        }
        catch(Exception e){
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
            }
            if (_servidor != null)
                _servidor.fin();
            return;
        }

    }

    public List getMensajes(){
        return _mensajes;
    }

    private void log(String x){
        System.out.println(x);
    }
}