package p.pruebas;

import java.net.*;
import java.util.*;
import java.io.*;

public class Servidor{

    public static final int PORT = 4001;

    public static void main(String[] args){
        Servidor rs = new Servidor();
        rs.startListening();
    }

    public Servidor(){
    }

    public void startListening(){
        ServerSocket ss = null;
        try{
//            boolean seguir = true;
//            int port = PORT;
//            while (seguir){
//                try {
                    ss = new ServerSocket(PORT);
//                    seguir = false;
//                }
//                catch (Exception ex) {
//
//                    System.out.println("falló co port = " + port );
//                    port++;
//                    ex.printStackTrace();
//                    if (port == 6000)
//                        System.exit(0);
//                }
//
//            }

            System.out.println("Esperando conexión.........");
            while(true){
                Socket s = ss.accept();

                Executor e = new Executor(s);
                e.start(); //con esto se manda a correr al método run del
                           //Thread y esta clase sigue eschuchando pedidos de
                           //otros clientes
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    class Executor extends Thread{

        private Socket socket;
        ObjectInputStream in = null;
        String command = "";

        public Executor(Socket s){
            socket = s;
            try {

            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        public void run(){
            String command = "";
            try{
                while(true){
                    in = new ObjectInputStream(socket.getInputStream());
                    System.out.println("conectadoooooooooooooooooooooooooo");
                    command = (String)in.readObject();
                    System.out.println(command);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }

    }
}