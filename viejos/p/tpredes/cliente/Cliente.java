package p.tpredes.cliente;

import java.net.*;
import java.util.*;
import java.io.*;

public class Cliente {
    private int _port = 4999;

    public Cliente() {
    }

    public static void main(String[] args){
        Cliente rs = new Cliente();
        rs.startListening();
    }

    public void startListening(){
        Vector recibido = new Vector();
        try{
            ServerSocket ss = new ServerSocket(_port);

            while(true){
                log("Esperando conexión en el puerto " + _port);
                Socket s = ss.accept();
                log ("Se estableció la conexión");
                mostrar(s);
            }
            /*-----*/
//            InputStream is = s.getInputStream();
//            while (!s.isClosed()){
//                System.out.println("está conectado ");
//                byte[] b = new byte[1];
//                is.read(b);
//                Byte by = new Byte(b[0]);
//                recibido.add(by);
//                System.out.println("llegó = " + ava);

//            }
//            Transmisor e = new Transmisor(s);
//            e.start();
        }
        catch(Exception e){
            System.out.println("---Exception *----");
            System.out.print("llegó: ");
            byte[] m = new byte[recibido.size()];
            for (int i = 0; i < recibido.size(); i++) {
                m[i] = ((Byte)recibido.elementAt(i)).byteValue();
            }
            System.out.println(" '" + new String(m) + "'");

            e.printStackTrace();
        }
    }

    private void mostrar(Socket s){
        try {
            System.out.println("mostrar");
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            Vector vec = (Vector) in.readObject();
            Integer inte = (Integer )vec.elementAt(0);
            System.out.println("   " + inte);
            for (int i = 1; i < vec.size(); i++) {
                String tem = (String) vec.elementAt(i);
                System.out.println("    " + tem);
            }

        }
        catch (Exception ex) {

        }
    }

    private void log(String x){
        System.out.println(x);
    }
}